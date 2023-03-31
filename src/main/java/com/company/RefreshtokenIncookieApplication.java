package com.company;

import com.company.config.AppEnv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = AppEnv.class)
public class RefreshtokenIncookieApplication {

    public static void main(String[] args) {
        SpringApplication.run(RefreshtokenIncookieApplication.class, args);
    }

}
