package org.boostcourse.config;

import org.boostcourse.argumentresolver.HeaderMapArgumentResolver;
import org.boostcourse.interceptor.LogInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"org.boostcourse.controller"})

public class WebMvcContextConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        final int cachePeriod = 31556926;

        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:META-INF/resourses/webjars").setCachePeriod(cachePeriod);
        registry.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(cachePeriod);
        registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(cachePeriod);
        registry.addResourceHandler("/img/**").addResourceLocations("/img/").setCachePeriod(cachePeriod);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        System.out.println("addViewController가 호출됩니다.");
        registry.addViewController("/guestbook/").setViewName("index");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        System.out.println("아규먼트 리졸버 등록");
        argumentResolvers.add(new HeaderMapArgumentResolver());
    }

    @Bean   // 인터셉터 등록
    public InternalResourceViewResolver getInternalResourceViewResolver(){
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");

        return resolver;
    }
}
