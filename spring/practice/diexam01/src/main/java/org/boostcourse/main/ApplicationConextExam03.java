package org.boostcourse.main;

import org.boostcourse.config.ApplicationConfig;
import org.boostcourse.bean.Car;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationConextExam03 {
    public static void main(String[] args){
        ApplicationContext ac = new AnnotationConfigApplicationContext(ApplicationConfig.class); // AnnotationConfigApplicationContext는 JavaConfig클래스를 읽어들여 IoC와 DI를 적용

        // Car car = (Car) ac.getBean("car"); // getBean에 전달하는 "car"는 config 클래스의 메소드명
        Car car = ac.getBean(Car.class);// 파라미터로 요청하는 클래스 타입을 전달하면 형변환할 필요가 없고, config 파일의 메소드명에 오타가 발생해도 메소드의 리턴 타입을 확인하여 레퍼런스를 반환함.
        car.run();
    }
}
