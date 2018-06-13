package cn.easy.xinjing.api.base;

import cn.easy.base.api.BaseApiController;
import cn.easy.base.bean.common.ApiResultBean;
import cn.easy.base.config.utils.CustomUser;
import cn.easy.base.domain.Area;
import cn.easy.base.domain.User;
import cn.easy.base.service.*;
import cn.easy.base.utils.BaseUtils;
import cn.easy.base.utils.DateTimeUtil;
import cn.easy.base.utils.SpringSecurityUtils;
import cn.easy.xinjing.bean.api.*;
import cn.easy.xinjing.domain.*;
import cn.easy.xinjing.service.*;
import cn.easy.xinjing.utils.Constants;
import com.foxinmy.easemob4j.EasemobProxy;
import com.foxinmy.easemob4j.exception.EasemobException;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springside.modules.utils.text.TextValidator;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by chenzhongyi on 16/9/18.
 */
public class XjBaseApiController extends BaseApiController {
    @Autowired
    protected UserService userService;
    @Autowired
    protected UserDoctorService userDoctorService;
    @Autowired
    protected UserPatientService userPatientService;
    @Autowired
    protected AliSmsService smsService;
    @Autowired
    protected Environment env;
    @Autowired
    protected GtDoctorPushService gtDoctorPushService;
    @Autowired
    protected GtPatientPushService gtPatientPushService;
    @Autowired
    protected AppTokenService appTokenService;
    @Autowired
    protected ConfigService configService;
    @Autowired
    protected PrescriptionService prescriptionService;
    @Autowired
    protected PrescriptionContentService prescriptionContentService;
    @Autowired
    protected OrderService orderService;
    @Autowired
    protected AttachmentService attachmentService;
    @Autowired
    protected ContentService contentService;
    @Autowired
    protected DiseaseService diseaseService;
    @Autowired
    protected TherapyService therapyService;
    @Autowired
    protected ContentCollectService contentCollectService;
    @Autowired
    protected OssMtsService ossMtsService;
    @Autowired
    protected AreaService areaService;
    @Autowired
    protected DoctorPatientService doctorPatientService;
    @Autowired
    protected HospitalService hospitalService;

    @Autowired
    EntityManager entityManager;
    @Autowired
    CacheManager cacheManager;


    protected CustomUser getCurrentUser() {
        return SpringSecurityUtils.getCurrentUser();
    }

    protected String getCurrentUsername() {
        return SpringSecurityUtils.getCurrentUsername();
    }

    protected String getCurrentUserId() {
        return SpringSecurityUtils.getCurrentUserId();
    }

    protected ApiResultBean<Map<String, Object>> doLogin(LoginBean loginBean, int type, HttpServletRequest request) {
        User user = userService.getByUsername(loginBean.getUsername());
        if (user == null) {
            return toError("用户未注册", -3);
        }

        if (!BaseUtils.matchesPassword(loginBean.getPassword(), user.getPassword())) {
            return toError("密码不正确", -2);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("userId", user.getId());
        data.put("username", user.getUsername());
        data.put("realname", user.getRealname());
        if(type != Constants.USER_TYPE_CONTROLDOCTOR_APP){
            data.put("password", loginBean.getPassword());
            data.put("encryptPw", user.getPassword());
        }


        //设置app auth信息
        AppToken appToken = new AppToken();
        appToken.setChannel(loginBean.getChannel());
        appToken.setAppId(loginBean.getAppId());
        appToken.setAppVersion(loginBean.getAppVersion());
        appToken.setDeviceModel(loginBean.getDeviceModel());
        appToken.setDeviceSystem(loginBean.getDeviceSystem());
        appToken.setDeviceVersion(loginBean.getDeviceVersion());

        appToken.setCreatedAt(new Date());
        appToken.setUserId(user.getId());
        appToken.setUserType(type);
        appToken = appTokenService.save(appToken);
        data.put("token", appToken.getId());

        //如果是线下医生登陆返回医院字段
        if(type == Constants.USER_TYPE_CONTROLDOCTOR_APP){
            UserDoctor userDoctor = userDoctorService.getByUserId(user.getId());
            Hospital hospital =  hospitalService.getOne(userDoctor.getHospital());
            data.put("hospital",hospital.getName());
        }

        //更新设备信息
        if (type == Constants.USER_TYPE_DOCTOR||type == Constants.USER_TYPE_CONTROLDOCTOR_APP) {
            UserDoctor userDoctor = userDoctorService.getByUserId(user.getId());
            if(userDoctor == null) {
                return toError("账号类型不正确", -1);
            }
            if (userDoctor.getStatus() == Constants.DOCTOR_STATUS_INIT) {
                return toError("未认证", -4, data);
            } else if (userDoctor.getStatus() == Constants.DOCTOR_STATUS_TO_CHECK) {
                return toError("已提交", -5, data);
            } else if (userDoctor.getStatus() == Constants.DOCTOR_STATUS_NO_PASS) {
                return toError("未通过", -6, data);
            } else if (userDoctor.getStatus() == Constants.DOCTOR_STATUS_STOP) {
                return toError("已停诊", -7, data);
            }

            userDoctor.setPushId(loginBean.getPushId());
            userDoctorService.save(userDoctor);

            //消息推送别名绑定
            gtDoctorPushService.bindAlias(user.getUsername(), loginBean.getPushId());
        } else {
            UserPatient userPatient = userPatientService.getByUserId(user.getId());
            if(userPatient == null) {
                return toError("账号类型不正确", -1);
            }
            userPatient.setPushId(loginBean.getPushId());
            userPatientService.save(userPatient);

            //消息推送别名绑定
            gtPatientPushService.bindAlias(user.getUsername(), loginBean.getPushId());
        }
        user.setLastLoginAt(DateTimeUtil.toTimestamp(new Date()));
        userService.save(user);

        return toSuccess("登录成功", data);
    }

    protected ApiResultBean<Map<String, Object>> doLoginto(LoginBean loginBean, int type, HttpServletRequest request) {
        User user = userService.getByUsername(loginBean.getUsername());
        if (user == null) {
            return toError("用户未注册", -3);
        }



        Map<String, Object> data = new HashMap<>();
        data.put("userId", user.getId());
        data.put("username", user.getUsername());
        data.put("realname", user.getRealname());
        data.put("password", loginBean.getPassword());
        data.put("encryptPw", user.getPassword());
        data.put("headPictureUrl",null);
        //设置app auth信息
        AppToken appToken = new AppToken();
        appToken.setChannel(loginBean.getChannel());
        appToken.setAppId(loginBean.getAppId());
        appToken.setAppVersion(loginBean.getAppVersion());
        appToken.setDeviceModel(loginBean.getDeviceModel());
        appToken.setDeviceSystem(loginBean.getDeviceSystem());
        appToken.setDeviceVersion(loginBean.getDeviceVersion());

        appToken.setCreatedAt(new Date());
        appToken.setUserId(user.getId());
        appToken.setUserType(type);
        appToken = appTokenService.save(appToken);
        data.put("token", appToken.getId());

        //更新设备信息
        if (type == Constants.USER_TYPE_DOCTOR) {
            UserDoctor userDoctor = userDoctorService.getByUserId(user.getId());
            if(userDoctor == null) {
                return toError("账号类型不正确", -1);
            }
            if (userDoctor.getStatus() == Constants.DOCTOR_STATUS_INIT) {
                return toError("未认证", -4, data);
            } else if (userDoctor.getStatus() == Constants.DOCTOR_STATUS_TO_CHECK) {
                return toError("已提交", -5, data);
            } else if (userDoctor.getStatus() == Constants.DOCTOR_STATUS_NO_PASS) {
                return toError("未通过", -6, data);
            } else if (userDoctor.getStatus() == Constants.DOCTOR_STATUS_STOP) {
                return toError("已停诊", -7, data);
            }

            userDoctor.setPushId(loginBean.getPushId());
            userDoctorService.save(userDoctor);

            //消息推送别名绑定
            gtDoctorPushService.bindAlias(user.getUsername(), loginBean.getPushId());
        } else {
            UserPatient userPatient = userPatientService.getByUserId(user.getId());
            if(userPatient == null) {
                return toError("账号类型不正确", -1);
            }
            userPatient.setPushId(loginBean.getPushId());
            userPatientService.save(userPatient);

            //消息推送别名绑定
            gtPatientPushService.bindAlias(user.getUsername(), loginBean.getPushId());
        }
        user.setLastLoginAt(DateTimeUtil.toTimestamp(new Date()));
        userService.save(user);

        return toSuccess("登录成功", data);
    }

    protected void doLogout(ApiBaseBean apiBaseBean) {
        AppToken appToken = appTokenService.getOne(apiBaseBean.getToken());
        if (appToken != null) {
            appToken.setStatus(-1);
            appToken.setLogoutAt(new Date());
            appTokenService.save(appToken);
        }
    }

    protected ApiResultBean doSendCode(String mobile, HttpServletRequest request) {
        //TODO 增加 防刷功能
        if (!TextValidator.isMobileSimple(mobile)) {
            return toError("手机号码格式不正确", -1);
        }
        int captcha = 0;
        try {
            captcha = smsService.sendCode(mobile, "SMS_16730201");
            logger.info("mobile==" + mobile + ", get captcha====" + captcha);

            setCaptcha(captcha, mobile);
        } catch (Exception e) {
            return toError(e.getMessage(), -1);
        }
        //暂时打出验证码给app测试
        return toSuccess("验证码发送成功",captcha);
    }

    protected ApiResultBean doResetPassword(ResetPasswordBean modifyPasswordBean, HttpServletRequest request) {
        if (!validateCaptcha(modifyPasswordBean.getCaptcha(), modifyPasswordBean.getUsername())) {
            logger.info("captcha=====" + modifyPasswordBean.getCaptcha() + ", session captcha====" + getCaptcha(modifyPasswordBean.getUsername()) + ", valid failed");
            return toError("验证码错误", -1);
        }

        User dbUser = userService.getByUsername(modifyPasswordBean.getUsername());
        dbUser.setPassword(BaseUtils.encodePassword(modifyPasswordBean.getPassword()));
        userService.save(dbUser);
        //重置环信IM密码
        resetIMPassword(dbUser);
        return toSuccess("密码找回成功");
    }

    /**
     * 重置环信IM密码
     * @param dbUser
     */
    private void resetIMPassword(User dbUser) {
//        try {
//            EasemobProxy easemobProxy = new EasemobProxy();
//            easemobProxy.resetUserPassword(dbUser.getUsername(), dbUser.getPassword());
//        } catch (EasemobException e) {
//            logger.error("IM 环信重置密码失败, username: " + dbUser.getUsername(), e);
//        }
    }

    /**
     * 注册环信IM用户
     * @param userName
     * @param encryptPw
     * @return
     */
    protected boolean registerIMUser(String userName, String encryptPw){
//        try {
//            EasemobProxy easemobProxy = new EasemobProxy();
//            com.foxinmy.easemob4j.model.User user = new com.foxinmy.easemob4j.model.User();
//            user.setUname(userName);
//            user.setPassword(encryptPw);
//            easemobProxy.createUser(user);
//        } catch (EasemobException e) {
//            logger.warn("IM 账号创建失败, username: " + userName, e);
//            return false;
//        }
        return true;
    }

    protected ApiResultBean doModifyPassword(ModifyPasswordBean modifyPasswordBean, HttpServletRequest request) {
        User dbUser = userService.getByUsername(getCurrentUsername());
        if (!BaseUtils.matchesPassword(modifyPasswordBean.getOldPassword(), dbUser.getPassword())) {
            return toError("旧密码错误", -1);
        }

        dbUser.setPassword(BaseUtils.encodePassword(modifyPasswordBean.getPassword()));
        userService.save(dbUser);
        //重置环信IM密码
        resetIMPassword(dbUser);
        return toSuccess("密码修改成功");
    }

    protected void setCaptcha(int captcha, String mobile) {
        Cache cache = cacheManager.getCache("captchaCache");
        cache.put(Constants.CAPTCHA_KEY + "_" + mobile, captcha);
    }

    protected String getCaptcha(String mobile) {
        Cache cache = cacheManager.getCache("captchaCache");
        Cache.ValueWrapper obj = cache.get(Constants.CAPTCHA_KEY + "_" + mobile);
        return obj == null ? null : StringUtils.trim(obj.get().toString());
    }

    protected boolean validateCaptcha(String captcha, String mobile) {
        if (StringUtils.isBlank(captcha)) {
            return false;
        }
        Object obj = getCaptcha(mobile);
        if (captcha.equals(obj)) {
            Cache cache = cacheManager.getCache("captchaCache");
            cache.evict(Constants.CAPTCHA_KEY + "_" + mobile);
            return true;
        }
        return false;
    }

    protected Map<String, Object> buildData(String key, Object value) {
        Map<String, Object> data = new HashMap<>();
        data.put(key, value);
        return data;
    }

    protected List<RegionBean> getRegionBean() {
        List<Area> provinceList = areaService.findByCodeLike("%0000");
        List<Area> cityList = areaService.findByCodeLike("%00");

        List<RegionBean> data = new ArrayList<>();
        for (Area province : provinceList) {
            RegionBean regionBean = RegionBean.valueOf(province);
            String prefix = province.getCode().substring(0, 2);
            Iterator<Area> it = cityList.iterator();
            while (it.hasNext()) {
                Area value = it.next();
                if (regionBean.getCode().equals(value.getCode())) {
                    continue;
                }
                if (value.getCode().startsWith(prefix)) {
                    regionBean.add(value);
                    it.remove();
                }
            }
            data.add(regionBean);
        }
        return data;
    }

    protected List<RegionBean> getRegionBean(List<String> region02,List<String> region05) {
        List<Area> provinceList = areaService.findByCodeLike("%0000");
        List<Area> cityList = areaService.findByCodeLike("%00");

        List<RegionBean> data = new ArrayList<>();
        for (String region02s : region02) {
            for (Area province : provinceList) {
                RegionBean regionBean = RegionBean.valueOf(province);
                String prefix = province.getCode().substring(0, 2);
                if(prefix.equals(region02s)){
                    Iterator<Area> it = cityList.iterator();
                    while (it.hasNext()) {
                        Area value = it.next();
                        if (regionBean.getCode().equals(value.getCode())) {
                            continue;
                        }
                        if (value.getCode().startsWith(prefix)) {
                            for (String region05s : region05) {
                                if(value.getCode().equals(region05s)){
                                    regionBean.add(value);
                                    it.remove();
                                    break;
                                }
                            }

                        }
                    }
                    data.add(regionBean);
                    break;
                }


            }
        }

        return data;
    }


    protected List<LetterToArrayBean> getDisease() {
        List<Disease> diseaseList = diseaseService.findAll();

        Map<String, List> mapList = new TreeMap<>(new Comparator<String>() {
            public int compare(String obj1, String obj2) {
                return obj1.compareTo(obj2);
            }
        });

        for (Disease disease : diseaseList) {
            if (Constants.DISEASE_STATUS_PUBLISH != disease.getStatus()) {
                continue;
            }
            try {
                String firstLetter = getFirstLetter(disease.getName());
                if (mapList.containsKey(firstLetter)) {
                    mapList.get(firstLetter).add(disease);
                } else {
                    List<Disease> list = new ArrayList<>();
                    list.add(disease);
                    mapList.put(firstLetter, list);
                }
            } catch (PinyinException e) {
                e.printStackTrace();
            }
        }

        return LetterToArrayBean.valueOf(mapList);
    }

    protected String getFirstLetter(String name) throws PinyinException {
        return PinyinHelper.convertToPinyinString(name, "", PinyinFormat.WITHOUT_TONE).toUpperCase().substring(0, 1);
    }

    protected Map<String, String> getUserIdAndNameData(@RequestBody PhoneBean phoneBean) {
        User user = userService.getByUsername(phoneBean.getPhone());
        UserDoctor userDoctor = userDoctorService.getByUserId(user.getId());
        Map<String, String> data = new HashMap<>();
        data.put("phone", phoneBean.getPhone());
        data.put("id", user.getId());
        data.put("realname", user.getRealname());
        data.put("headpictureurl",userDoctor==null?null:userDoctor.getHeadPictureUrl());
        return data;
    }

    protected String getConfigValue(String code, String key) {
        if (StringUtils.isBlank(key)) {
            return "";
        }
        return (String) this.getConfigMap(code).get(key);
    }

    protected String getConfigValue(String code, Integer key) {
        if (key == null) {
            return "";
        }
        return getConfigValue(code, key.toString());
    }
}
