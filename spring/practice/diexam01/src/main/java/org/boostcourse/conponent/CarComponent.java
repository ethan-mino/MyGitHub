package org.boostcourse.conponent;

import org.boostcourse.bean.Engine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component  // 컴포넌트 스캔의 대상이 되는 어노테이션 중 하나로써 주로 유틸, 기타 지원 클래스에 붙이는 어노테이션
public class CarComponent {
    @Autowired  // 주입 대상이되는 bean을 컨테이너에서 찾아 주입하는 어노테이션
                // Engine 타입으로 생성된 객체가 있으면 v8에 자동으로 주입, 따라서 v8의 setter 메소드가 없어도 됨.
    private EngineComponent v8;

    public CarComponent(){
        System.out.println("CarComponent 생성자");
    }

    public void run(){
        System.out.println("엔진을 이용하여 달립니다.");
        v8.exec();
    }
}
