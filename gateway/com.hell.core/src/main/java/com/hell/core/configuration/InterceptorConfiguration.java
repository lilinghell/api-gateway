package com.hell.core.configuration;

import com.hell.core.interceptor.BaseHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Set;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    private Set<BaseHandlerInterceptor> interceptors;

    @Bean
    @Description("国际化")
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("_locale");

        return localeChangeInterceptor;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new SessionLocaleResolver();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor()).addPathPatterns("/**");//国际化
        if (null != interceptors) {
            interceptors.forEach(interceptor -> {
                registry.addInterceptor(interceptor)
                        .addPathPatterns(interceptor.getPathPatterns())
                        .excludePathPatterns(interceptor.getExcludePathPatterns());
            });
        }
    }

    @Autowired(required = false)
    public void setInterceptors(Set<BaseHandlerInterceptor> interceptors) {
        this.interceptors = interceptors;
    }
}
