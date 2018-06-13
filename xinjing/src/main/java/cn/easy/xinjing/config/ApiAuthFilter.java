package cn.easy.xinjing.config;

import cn.easy.base.SpringContext;
import cn.easy.base.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chenzhongyi on 16/9/18.
 */
@Component
public class ApiAuthFilter implements Filter {
    private Logger logger		= LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("ApiAuthFilter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        logger.info("ApiAuthFilter destroy");
    }

    private boolean isContains(String excludeUrls, String url) {
        if(StringUtils.isBlank(excludeUrls)) {
            return false;
        }
        List<String> excludeUrlList = Arrays.asList(excludeUrls.split(","));
        return excludeUrlList.contains(url);
    }

}
