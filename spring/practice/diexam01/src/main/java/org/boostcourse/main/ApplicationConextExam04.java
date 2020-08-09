package org.boostcourse.main;

import org.boostcourse.config.ApplicationConfig2;
import org.boostcourse.conponent.CarComponent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationConextExam04 {
    public static void main(String[] args){
        ApplicationContext ac = new AnnotationConfigApplicationContext(ApplicationConfig2.class); // AnnotationConfigApplicationContext는 JavaConfig클래스를 읽어들여 IoC와 DI를 적용
                                                                                                  // 이때 AnnotationConfigApplicationContext는 설정 파일 중에 @Bean이 붙어 있는 메소드들을 자동으로 실행하여 그 결과로 리턴하는 객체들을 기본적으로 싱글턴으로 관리.

        //Car car = (Car) ac.getBean("car"); // getBean에 전달하는 "car"는 config 클래스의 메소드명
        CarComponent carComponent = ac.getBean(CarComponent.class);// 파라미터로 요청하는 클래스 타입을 전달하면 형변환할 필요가 없고, config 파일의 메소드명에 오타가 발생해도 메소드의 리턴 타입을 확인하여 레퍼런스를 반환함.
        carComponent.run();
    }
}
