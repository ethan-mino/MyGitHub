package org.boostcourse.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@ComponentScan(basePackages = {"org.boostcourse.service", "org.boostcourse.dao"})
@Import({DbConfig.class})
public class ApplicationConfig {
}
