package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
//  Spring MVC와 Swagger2의 기본 설정이 자동으로 설정 된다.
@EnableWebMvc   // Spring MVC설정
@ComponentScan(basePackages = {"controller", "exception"})
public class WebMvcContextConfig implements WebMvcConfigurer {
    /*
        @Override
        public void configureViewResolvers(ViewResolverRegistry registry) {
            registry.jsp("/WEB-INF/view/", ".jsp");
        }
    */
    @Bean
    public InternalResourceViewResolver getInternalResourceViewResolver(){
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Bean
    public MultipartResolver multipartResolver(){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(1048 * 1048 * 10);   // 최대 10MB

        return multipartResolver;
    }

    // DefaultServlet에 대한 설정을 합니다.
    // DispatcherServlet이 처리하지 못하는 URL은 DefaultServlet이 처리하게 됩니다.
    // 해당 설정이 없으면 자동 생성된 Swaager 페이지를 볼 수 없다.
    //@Override
    //public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    //    configurer.enable();
    //}

    /*
        Swagger2를 사용하려면 Docket객체를 Bean으로 설정해야 한다.
        Docker객체에는 어떤 경로의 Web API들을 자동으로 문서화 할 것인지에 대한 설정과 문서 설명에 대한 내용이 포함된다.
        Swagger 사용 시에는 Docket Bean을 품고있는 설정 클래스 1개가 기본으로 필요하다.
        Spring Boot에서는 이 기본적인 설정파일 1개로 Swagger 와 Swagger UI 를 함께 사용 가능하지만,
        Spring MVC 의 경우 Swagger UI 를 위한 별도의 설정이 필요하다.
        이는, Swagger UI 를 ResourceHandler에 수동으로 등록해야 하는 작업인데,
        Spring Boot 에서는 이를 자동으로 설정해주지만 Spring MVC 에서는 그렇지 않기 때문이다.
    */

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        final int cachePeriod = 31556926;
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:META-INF/resourses/webjars").setCachePeriod(cachePeriod);
        registry.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(cachePeriod);
        registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(cachePeriod);
        registry.addResourceHandler("/img/**").addResourceLocations("/img/").setCachePeriod(cachePeriod);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("main");
    }

}