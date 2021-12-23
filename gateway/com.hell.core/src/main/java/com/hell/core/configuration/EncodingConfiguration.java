package com.hell.core.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@SuppressWarnings("unchecked")
@Configuration
public class EncodingConfiguration implements WebMvcConfigurer {

    @Bean
    @Description("乱码过滤器")
    public CharacterEncodingFilter encodingFilter() {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);
        return encodingFilter;
    }

    @Bean
    public FilterRegistrationBean encodingFilterRegistration() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean(encodingFilter());
        filterRegistration.getUrlPatterns().clear();
        filterRegistration.addUrlPatterns("/*");
        return filterRegistration;
    }
}
