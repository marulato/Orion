package org.orion.configration;

import org.orion.common.filter.AuthoritySecurityInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class AuthoritySecurityConfig implements WebMvcConfigurer {
    @Resource
    private AuthoritySecurityInterceptor authoritySecurityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authoritySecurityInterceptor).addPathPatterns("/**").
                excludePathPatterns("/web/AuthLogin", "/web/doLogin",
                        "/static/**", "/css/**",  "/fonts/**", "/images/**", "/js/**", "/scss/**", "/vendors/**");
    }
}
