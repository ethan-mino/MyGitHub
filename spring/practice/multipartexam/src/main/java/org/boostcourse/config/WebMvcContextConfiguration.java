package org.boostcourse.config;

import org.boostcourse.interceptor.LogInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"org.boostcourse.controller"})
public class WebMvcContextConfiguration implements WebMvcConfigurer {
    /*
        @Bean
        public InternalResourceViewResolver getInternalResourceViewResolver(){
            InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
            internalResourceViewResolver.setPrefix("/WEB-INF/views/");
            internalResourceViewResolver.setSuffix(".jsp");

            return internalResourceViewResolver;
        }
    */

    @Bean
    public MultipartResolver multipartResolver(){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(1024 * 1024 * 10);

        return multipartResolver;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/views/", ".jsp");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/uploadform");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor());
    }
}
