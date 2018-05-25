package com.nix.cinema.common.config;

import com.nix.cinema.common.supper.PermissionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Kiss
 * @date 2018/05/01 20:39
 */
//标注此文件为一个配置项，spring boot才会扫描到该配置。该注解类似于之前使用xml进行配置
@Configuration
public class CustomWebMvcConfigurerAdapter implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //对来自/ 这个链接来的请求进行拦截
        registry.addInterceptor(new PermissionInterceptor()).addPathPatterns("/**");
    }
}