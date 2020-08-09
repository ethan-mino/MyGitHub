package org.boostcourse.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

@PropertySource("classpath:application.properties")
@Configuration
@EnableTransactionManagement
public class DbConfig implements TransactionManagementConfigurer {
    // mysql Driver 클래스 이름은 "com.mysql.jdbc.Driver"에서 버저 6 이후에는 "com.mysql.cj.jdbc.Driver"로 변경되었다.
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    // java.sql.SQLException : Cannot create PoolableConnectionFactory (The Server time zone value "KST" is unrecogized or represents more than one time zone. You must configure either the server or JDBC driver (via the 'serverTimezone' configuration property) to use a more specifc time zone value if you want to utilize time zone support.)
    // DB연결시 위와 같은 오류가 발생한다면, &serverTimezone=UTC를 url에 붙여줘야 한다.
    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;

    // 커넥션 풀과 관련된 Bean을 생성한다.
    @Bean
    public DataSource dataSource(){
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);

        return dataSource;
    }

    // 트랜잭션 관리자를 생성한다.
    @Bean
    public PlatformTransactionManager transactionManager(){
        return new DataSourceTransactionManager(dataSource());
    }

    @Override
    public TransactionManager annotationDrivenTransactionManager() {
        return transactionManager();
    }
}
