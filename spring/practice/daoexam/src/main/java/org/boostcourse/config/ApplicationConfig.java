package org.boostcourse.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {"org.boostcourse.dao"})  // basePackages를 지정하면 여러개의 package를 지정할 수 있다.
@Import({DBConfig.class})   // Import 어노테이션을 사용하면 설정 파일을 나누어서 작성할 수 있다.
                            // 데이터베이스 연결에 관련된 설정은 따로 작성함.
public class ApplicationConfig {
}
