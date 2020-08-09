package org.boostcourse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"org.boostcourse.controller"})
// DispatcherServlet이 실행될 때 읽어들이는 설정 파일
public class WebMvcContextConfiguration extends WebMvcConfigurerAdapter {   // @EnableWebMvc를 이용하면 기본적인 설정이 모두 자동으로 되지만, 기본 설정 이외의 설정이 필요할 경우 헤당 클래스를 상속 받을 후, 메소드를 오버라이딩 하여 구현한다.

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) { // 모든 요청이 들어왔을 때, DispatcherServlet이 실행되도록 설정해주었기 때문에, 정적 리소스는 addResourceHandler와 addResourceLocations를 사용하여 매핑 URL과 정적 리소스의 위치를 설정한다. (https://atoz-develop.tistory.com/entry/Spring-MVC-Static-Resources%EC%A0%95%EC%A0%81-%EB%A6%AC%EC%86%8C%EC%8A%A4-%EC%84%A4%EC%A0%95-%EB%B0%A9%EB%B2%95 참조)
        int cachePeriod = 31556926;
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:META-INF/resources/webjars/").setCachePeriod(cachePeriod);;
        registry.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(cachePeriod);
        registry.addResourceHandler("/img/**").addResourceLocations("/img/").setCachePeriod(cachePeriod);;
        registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(cachePeriod);;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();    // default servlet handler를 사용하도록 해줌. (mapping 정보가 없는 URL 요청은 Spring의 default servlet http request handler가 처리하도록 해줌)
                                // spring의 default servlet http request handler는 Was의 default servlet에게 해당 일을 넘기고, was는 default servlet이 static한 자원을 읽어서 보여줌.
    }

    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        System.out.println("addViewController가 호출됩니다.");
        registry.addViewController("/").setViewName("main");    // 특정 URL에 대한 처리를 controller 클래스를 작성하지 않고 매핑할 수 있도록 해줌.
                                                                       // 요청 자체가 '/'로 들어오면, "main"이라는 이름의 view로 보여줌.
                                                                       // "main"이라는 이름만 가지고서는 view 정보를 찾아낼 수 없고, view 정보는 getInternalResourceViewResolver()라는 메서드에서 설정된 형태로 view를 사용하게 됨.
    }

    @Bean
    public InternalResourceViewResolver getInternalResourceViewResolver(){
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp"); // /WEB-INF/views/main.jsp
        return resolver;
    }
}
