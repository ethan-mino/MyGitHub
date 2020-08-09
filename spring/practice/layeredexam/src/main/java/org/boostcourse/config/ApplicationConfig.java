package org.boostcourse.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {"org.boostcourse.dao", "org.boostcourse.service"})
@Import({DBConfig.class})

public class ApplicationConfig {
}
