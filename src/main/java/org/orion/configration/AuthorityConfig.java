package org.orion.configration;

import org.orion.common.filter.AuthorityInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class AuthorityConfig implements WebMvcConfigurer {

    @Resource
    private AuthorityInterceptor authorityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorityInterceptor).addPathPatterns("/**").excludePathPatterns("/web/AuthLogin", "/web/AuthLogin/signin", "/web/AuthLogin/logout");
    }
}
