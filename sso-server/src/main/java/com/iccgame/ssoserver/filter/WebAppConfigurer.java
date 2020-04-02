package com.iccgame.ssoserver.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//@Configuration
public class WebAppConfigurer extends WebMvcConfigurerAdapter {
    @Autowired
    private RequestParamVerifyInterceptor requestParamVerifyInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestParamVerifyInterceptor).addPathPatterns("/**");
    }
}
