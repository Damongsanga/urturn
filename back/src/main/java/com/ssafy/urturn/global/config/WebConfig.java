package com.ssafy.urturn.global.config;

import com.ssafy.urturn.global.auth.ReferrerCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new ReferrerCheckInterceptor());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // favicon.ico 요청을 비어 있는 응답으로 처리
        registry.addViewController("/favicon.ico").setStatusCode(HttpStatus.NO_CONTENT);

        // /error 요청을 비어 있는 응답으로 처리
        registry.addViewController("/error").setStatusCode(HttpStatus.NO_CONTENT);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("k10a206.p.ssafy.io:3001", "https://k10a206.p.ssafy.io", "http://localhost:3000")
            .allowedHeaders("Authorization", "content-type")
            .allowedMethods("GET", "POST", "DELETE", "PATCH", "OPTIONS")
            .allowCredentials(true);
    }

}

