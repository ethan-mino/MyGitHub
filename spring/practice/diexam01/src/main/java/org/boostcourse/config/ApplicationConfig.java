package org.boostcourse.config;

import org.boostcourse.bean.Car;
import org.boostcourse.bean.Engine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//@Configuration // 스프링 설정 클래스를 선언하는 어노테이션. AnnotationConfigApplicationContext는 자바 config Class를 읽어들여서 IOC와 DI를 적용한다.
               // AnnotaionConfigApplicationContext는 @Bean annotaion이 붙어있는 메소드들을 자동으로 실행해서 그 결과로 리턴하는 객체들을 싱글톤으로 관리.
public class ApplicationConfig {
    @Bean   // bean을 정의하는 어노테이션
            // ApplicationContext는 파라미터가 없는 Bean 생성 매서드를 먼저 다 실행한 후, 반환받은 객체를 관리하고, 파라미터에 생성된 객체들과 같은 타입의 객체가 있을 경우에 파라미터로 전달해서 객체를 생성함.
            // 즉, Engine 객체가 먼저 만들어진 후에, car 메소드의 파리미터로 전달됨.
    public Car car(Engine e){
        Car c = new Car();
        c.setEngine(e);
        return c;
    }
    @Bean
    public Engine engin(){
        return new Engine();
    }

}
