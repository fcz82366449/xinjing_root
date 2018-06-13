package cn.easy.xinjing.config;

import com.google.common.collect.Lists;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by chenzhongyi on 16/9/18.
 */
@Configuration
public class ApiFilterConfig {

    @Bean
    FilterRegistrationBean wechatAuthFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new ApiAuthFilter());
        bean.setUrlPatterns(Lists.newArrayList("/api/*"));
        return bean;
    }

}
