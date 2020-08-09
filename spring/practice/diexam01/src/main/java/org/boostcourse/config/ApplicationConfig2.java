package org.boostcourse.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.boostcourse")   // @Controller, @Service, @Repository, @Component 어노테이션이 붙은 클래스를 찾아 컨테이너에 반으로 록하는 어노테이션
                                    // 반드시 ComponentScan을 할 패키지명을 전달. (파라미터로 들어온 패키지 이하에서 @Controller, @Service, @Repository, @Component 어노테이션이 붙어 있는 클래스를 찾아 메모리에 모두 올려줌)
                                    // controler, service, repository, commponent와 같은 annotation이 붙어있는 class를 bean으로 등록함.
public class ApplicationConfig2 {

}
