package cn.easy.xinjing.aop;

import cn.easy.base.bean.common.ApiResultFactory;
import cn.easy.base.config.utils.CustomUser;
import cn.easy.base.domain.User;
import cn.easy.base.service.UserService;
import cn.easy.base.utils.DateTimeUtil;
import cn.easy.base.utils.Servlets;
import cn.easy.base.utils.SpringSecurityUtils;
import cn.easy.xinjing.bean.api.ApiBaseBean;
import cn.easy.xinjing.domain.AppToken;
import cn.easy.xinjing.service.AppTokenService;
import cn.easy.xinjing.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenzhongyi on 2016/10/10.
 */
@Aspect
@Component
public class ApiTokenAspect {
    public static final Map<Integer, String> userType2AuthUrl = new HashMap<>();

    static {
        userType2AuthUrl.put(Constants.USER_TYPE_DOCTOR, "/api/*/doctor/**");
        userType2AuthUrl.put(Constants.USER_TYPE_PATIENT, "/api/*/patient/**");
        userType2AuthUrl.put(Constants.USER_TYPE_VR_ROOM, "/api/*/vrRoom/**");
        userType2AuthUrl.put(Constants.USER_TYPE_APP_VR_ROOM, "/api/*/appVrRoom/**");
        userType2AuthUrl.put(Constants.USER_TYPE_APP_CONTROL_VR_ROOM, "/api/*/appControlVrRoom/**");
        userType2AuthUrl.put(Constants.USER_TYPE_APP_VR_ROOM_CASE, "/api/*/VrRoomCaseNo/**");
        userType2AuthUrl.put(Constants.USER_TYPE_CONTROLDOCTOR_APP, "/api/*/appControlDoctor/**");

    }

    @Autowired
    private AppTokenService appTokenService;
    @Autowired
    private UserService userService;

    @Around("execution(public * cn.easy.base.api.BaseApiController+.*(..))")
    public Object aroundMethod(ProceedingJoinPoint point) throws Throwable {
        if (beforeMethod(point)) {
            Object retVal = point.proceed();
            afterMethod(point);
            return retVal;
        }
        return null;
    }


    public boolean beforeMethod(ProceedingJoinPoint point) throws IOException {
        ApiBaseBean apiBaseBean = null;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        for (Object arg : point.getArgs()) {
            if (arg instanceof ApiBaseBean) {
                apiBaseBean = (ApiBaseBean) arg;
            }
        }

        if (apiBaseBean == null) {
            apiBaseBean = new ApiBaseBean();
            apiBaseBean.setToken(request.getParameter("token"));
        }

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        ApiAuth apiAuth = method.getDeclaredAnnotation(ApiAuth.class);
        if (apiAuth == null) { //不需要权限限制
            return true;
        }

        if (StringUtils.isBlank(apiBaseBean.getToken())) {
            responseError(response, "未登录", 95);
            return false;
        } else {
            AppToken appToken = appTokenService.getOne(apiBaseBean.getToken());
            if (appToken == null) {
                responseError(response, "无效token", 96);
                return false;
            }
            if (appToken.getStatus() == Constants.APP_TOKEN_STATUS_INVALID) {
                responseError(response, "已经退出登录", 97);
                return false;
            }
            if (appToken.getCreatedAt().before(DateTimeUtil.addMonth(new Date(), -1))) {
                responseError(response, "登录已经失效", 98);
                return false;
            }

            if(!checkAuthUrl(request, response, appToken)) {
                responseError(response, "访问接口与用户类型不匹配", 99);
                return false;
            }

            return setAuthentication(response, appToken);
        }
    }

    public void afterMethod(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        ApiAuth apiAuth = method.getDeclaredAnnotation(ApiAuth.class);
        if (apiAuth == null) {
            return;
        }

        clearAuthentication();
    }

    /**
     * 判断用户类型与访问链接是否匹配
     * @param request
     * @param response
     * @param appToken
     * @return
     */
    public boolean checkAuthUrl(HttpServletRequest request, HttpServletResponse response, AppToken appToken) {
        PathMatcher pathMatcher = new AntPathMatcher();

        for(Map.Entry<Integer, String> entry : userType2AuthUrl.entrySet()) {
            if(appToken.getUserType().equals(entry.getKey()) && pathMatcher.match(entry.getValue(), request.getRequestURI())) {
                return true;
            }
        }

        return false;
    }

    /**
     * 设置用户权限信息
     * @param response
     * @param appToken
     * @return
     * @throws IOException
     *
     * TODO 与后台的SESSION冲突，待优化
     */
    private boolean setAuthentication(HttpServletResponse response, AppToken appToken) throws IOException {
        if (StringUtils.isNotBlank(SpringSecurityUtils.getCurrentUserId())) {
            return true;
        }
        User user = userService.findOne(appToken.getUserId());

        if (user == null) {
            responseError(response, "用户不存在", 99);
            return false;
        }

        CustomUser customUser = new CustomUser(user.getUsername(), user.getPassword(), user.getEnabled() == 1, true, true, user.getAccountNonLocked() == 1,
                AuthorityUtils.NO_AUTHORITIES);
        customUser.setId(user.getId());
        customUser.setOrganId(user.getOrganId());
        customUser.setAttempts(user.getAttempts());
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(customUser, null));
        return true;
    }

    /**
     * 清除用户权限信息
     * TODO 与后台的SESSION冲突，待优化
     */
    private void clearAuthentication() {
        if (StringUtils.isNotBlank(SpringSecurityUtils.getCurrentUserId())) {
            SecurityContextHolder.clearContext();
        }
    }

    /**
     * 输出错误信息
     * @param response
     * @param msg
     * @param code
     * @throws IOException
     */
    private void responseError(HttpServletResponse response, String msg, int code) throws IOException {
        Servlets.responseBodyByJson(response, ApiResultFactory.toError(msg, code));
    }
}
