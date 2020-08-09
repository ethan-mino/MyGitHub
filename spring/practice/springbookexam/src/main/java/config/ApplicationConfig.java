package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {"service", "mapper"})
@Import({DBConfig.class})
public class ApplicationConfig {

}
