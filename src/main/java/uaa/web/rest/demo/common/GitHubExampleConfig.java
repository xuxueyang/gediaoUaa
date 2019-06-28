package uaa.web.rest.demo.common;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//　在使用fallback属性时，需要使用@Component注解，保证fallback类被Spring容器扫描到,GitHubExampleConfig内容如下：

@Configuration
public class GitHubExampleConfig {
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
