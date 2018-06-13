package cn.easy.base.config;

import cn.easy.base.config.utils.CsrfSecurityRequestMatcher;
import cn.easy.base.config.utils.CustomJdbcUserDetailsManager;
import cn.easy.base.config.utils.CustomUser;
import cn.easy.base.domain.LoginLog;
import cn.easy.base.domain.User;
import cn.easy.base.service.ILicenseService;
import cn.easy.base.service.LoginLogService;
import cn.easy.base.service.UserService;
import cn.easy.base.utils.*;
import cn.easy.xinjing.utils.ProjectUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.JdbcUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	DataSource dataSource;
	@Autowired
	LoginLogService loginLogService;
	@Autowired(required = false)
	ILicenseService licenseService;
	@Autowired
	UserService userService;
	@Autowired
	MessageSource messageSource;
	@Value("${no_auth_urls}")
	private String noAuthUrls;
	@Value("${no_csrf_urls}")
	private String noCsrfUrls;

	final DeviceResolver deviceResolver = new LiteDeviceResolver();

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		String[] noAuthUrlArray = ArrayUtils.addAll(Global.STATICS, "/register", "/captcha/image", "/api/**", "/");
		if(StringUtils.isNotBlank(noAuthUrls)) {
			noAuthUrlArray = ArrayUtils.addAll(noAuthUrlArray, noAuthUrls.split(","));
		}

		http.authorizeRequests()
				.antMatchers(noAuthUrlArray)
				.permitAll().anyRequest().authenticated().and().formLogin().loginPage("/support/login")
				.failureUrl("/support/login?error").defaultSuccessUrl("/support").successHandler(new LoginSuccessHandler()).permitAll().and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/support/logout")).logoutSuccessUrl("/support/login?logout")
				.logoutSuccessHandler(new CustomLogoutSuccessHandler()).permitAll().and().rememberMe();

		// 控制一个帐号只允许一次登录
		http.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(false).expiredUrl("/support/login?expired");

		http.headers().frameOptions().disable();

		// 设置csrf验证
		http.csrf().requireCsrfProtectionMatcher(csrfSecurityRequestMatcher());
	}

	@Bean
    CsrfSecurityRequestMatcher csrfSecurityRequestMatcher() {
		CsrfSecurityRequestMatcher bean = new CsrfSecurityRequestMatcher();
		bean.addExecludeUrl("/api");
		bean.addExecludeUrl("/model");
		bean.addExecludeUrl("/attachment");
		bean.addExecludeUrl("/common/ueditor");
		if(StringUtils.isNotBlank(noCsrfUrls)) {
			for (String url : noCsrfUrls.split(",")) {
				bean.addExecludeUrl(url);
			}
		}
		return bean;
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.apply(new JdbcUserDetailsManagerConfigurer(customJdbcUserDetailsManager()))
				.authoritiesByUsernameQuery(getAuthoritiesQuery()).usersByUsernameQuery(getUsersQuery());
		auth.authenticationProvider(myAuthenticationProvider());
		auth.userDetailsService(customJdbcUserDetailsManager()); //解决rememberme错误的问题
	}

	@Bean
    CustomJdbcUserDetailsManager customJdbcUserDetailsManager() {
		CustomJdbcUserDetailsManager bean = new CustomJdbcUserDetailsManager();
		bean.setDataSource(dataSource);
		return bean;
	}

	@Bean
	DaoAuthenticationProvider myAuthenticationProvider() {
		DaoAuthenticationProvider myAuthenticationProvider = new MyAuthenticationProvider();
		myAuthenticationProvider.setUserDetailsService(customJdbcUserDetailsManager());
		myAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
		return myAuthenticationProvider;
	}

	class MyAuthenticationProvider extends DaoAuthenticationProvider {

		@Override
		public Authentication authenticate(Authentication authentication) throws AuthenticationException {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

//			String captcha = request.getParameter("captcha");
//			if(StringUtils.isBlank(captcha)) {
//				throw new InternalAuthenticationServiceException("请输入验证码");
//			}
//			else {
//				if(!captcha.equals(ProjectUtil.getSupportCaptcha(authentication.getPrincipal().toString()))) {
//					throw new InternalAuthenticationServiceException("验证码不正确");
//				}
//			}

			try {
				Authentication auth = super.authenticate(authentication);
				CustomUser currentUser = (CustomUser) auth.getPrincipal();
				if (licenseService != null) {
					int userCount = userService.countByEnabled();
					if (!licenseService.isIgnoreLicense(userCount) && !Constants.ADMIN_ID.equals(currentUser.getId())) {
						if (!licenseService.isValid(userCount)) {//无效授权许可
							throw new InternalAuthenticationServiceException(messageSource.getMessage("login.licenseInvalid", null, Locale.getDefault()));
						}//验证用户是否超出许可范围
						else if (!licenseService.loginValidUserCount(userCount)) {
							throw new InternalAuthenticationServiceException(messageSource.getMessage("login.userCountOver", null, Locale.getDefault()));
						} else if (!licenseService.loginValidDate()) {//验证许可证是否过期
							throw new InternalAuthenticationServiceException(messageSource.getMessage("login.licenseExpire", null, Locale.getDefault()));
						}
					}
				}

				userService.nonLocked(authentication.getName());
				return auth;
			} catch (BadCredentialsException e) {
				User user = userService.getByUsername(authentication.getName());
				if (user != null) {
					user.setAttempts(user.getAttempts() + 1);
					if(user.getAttempts() >= Constants.PASSWORD_RETRY_COUNT){
						user.setAccountNonLocked(Constants.FALSE);
					}
					else {
						request.getSession().setAttribute(Constants.EXTRA_INFORMATION, user);
					}
					userService.update(user);
				}
				throw e;
			}
		}
	}

	class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
		@Override
		public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
				Authentication authentication) throws ServletException, IOException {
			LoginLog loginLog = new LoginLog();
			loginLog.setBrowser(request.getHeader("User-Agent"));
			loginLog.convert2Device(deviceResolver.resolveDevice(request));
			loginLog.setIp(getRemoteAddrIp(request));
			loginLog.setPort(request.getRemotePort());
			loginLog.setSessionId(request.getSession().getId());
			loginLog.setType(1);
			loginLog.setUsername(SpringSecurityUtils.getCurrentUsername());
			//loginLog.setUserId(SpringSecurityUtils.getCurrentUserId());
			loginLogService.save(loginLog);

			//如果是明文密码，进行加密

			User user = userService.getByUsername(authentication.getName());
			user.setPassword(BaseUtils.encodePassword(user.getPassword()));
			user.setLastLoginAt(DateTimeUtil.toTimestamp(new Date()));
			userService.update(user);
			super.onAuthenticationSuccess(request, response, authentication);
		}
	}

	class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

		@Override
		public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
				Authentication authentication) throws IOException, ServletException {
			if (authentication == null) {
				return;
			}
			CustomUser customUser = (CustomUser) authentication.getPrincipal();

			LoginLog loginLog = new LoginLog();
			loginLog.setBrowser(request.getHeader("User-Agent"));
			loginLog.convert2Device(deviceResolver.resolveDevice(request));
			loginLog.setIp(getRemoteAddrIp(request));
			loginLog.setPort(request.getRemotePort());
			loginLog.setSessionId(request.getSession().getId());
			loginLog.setType(2);
			loginLog.setUsername(customUser.getUsername());
			//loginLog.setUserId(customUser.getId());
			loginLogService.save(loginLog);
			super.onLogoutSuccess(request, response, authentication);
		}
	}

	public static String getRemoteAddrIp(HttpServletRequest request) {
		String ipFromNginx = getHeader(request, "X-Real-IP");
		return StringUtils.isEmpty(ipFromNginx) ? request.getRemoteAddr() : ipFromNginx;
	}


	private static String getHeader(HttpServletRequest request, String headName) {
		String value = request.getHeader(headName);
		return StringUtils.isNotBlank(value) && !"unknown".equalsIgnoreCase(value) ? value : "";
	}

	private String getUsersQuery() {
		return "select username,password,enabled,id,organ_id,account_non_locked,attempts from pb_user u where u.hidden=0 AND u.username = ?";
	}

	private String getAuthoritiesQuery() {
		return "SELECT u.username, p.url as authority "
				+ "FROM pb_user u, pb_re_user_auth_role ur, pb_auth_role r, pb_re_auth_role_auth_permission rp,  pb_auth_permission p "
				+ "WHERE u.id=ur.user_id AND ur.auth_role_id = r.id AND r.id=rp.auth_role_id AND rp.auth_permission_id=p.id AND u.hidden=0 AND u.username=?";
	}
}
