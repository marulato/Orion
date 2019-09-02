package org.orion.configration;

import org.akita.crud.interceptor.SQLInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfig {
    @Bean
    public SQLInterceptor getSQLInterceptor() {
        return new SQLInterceptor();
    }
}
