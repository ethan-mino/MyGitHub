package org.shopcollector.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"org.shopcollector.service", "org.shopcollector.dao"})
public class ApplicationConfig {
}
