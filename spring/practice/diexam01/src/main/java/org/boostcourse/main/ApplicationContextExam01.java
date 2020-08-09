package org.boostcourse.main;

import org.boostcourse.bean.UserBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationContextExam01 {
    public static void main(String[] args) {
        // spring factory중 ApplicationContext 생성

        ApplicationContext ac  = new ClassPathXmlApplicationContext("classpath:applicationContext.xml"); // ApplicationContext는 인터페이스이고, ApplicationContext를 구현하고 있는 객체가 여러개 있다. 여기서는 ClassPathXmlApplicationContext를 사용
                                                                                                                     // classpath는 bean 정보를 가지고 있는 xml 파일의 path
                                                                                                                     // bean 정보를 가지고 factory를 생성
                                                                                                                     // classpath : applicationContext.xml와 같이 공백을 두면 안됨
                                                                                                                     // ClassPathXmlApplicationContext의 인스턴스가 생성될 때, 그 안에 선언된 bean들을 모두 메모리에 올림
        System.out.println("초기화 완료!");
        UserBean userBean = (UserBean)ac.getBean("userBean");   // 해당 객체의 레퍼런스를 반환 (getBean에 전달하는 문자열은 xml 파일의 bean 태그의 id를 나타낸다.)
                                                                   // getBean 메서드는 다양한 타입의 레퍼런스를 반환하므로 object 타입을 반환함. 따라서 형변환 해주어야함.
        userBean.setName("Gil");
        System.out.println(userBean.getName());

        UserBean userBean2 = (UserBean)ac.getBean("userBean");   // getBean에 전달하는 문자열은 xml 파일의 bean 태그의 id를 나타낸다.

        if(userBean == userBean2)
            System.out.println("같은 인스턴스입니다."); // spring ApplicationContext가 Bean 객체를 생성하는데 싱글톤 패턴을 사용하기 때문에 ApplicationContext의 getBean을 여러번 호출한다고 하더라도 같은 인스턴스를 반환한다.
    }
}
