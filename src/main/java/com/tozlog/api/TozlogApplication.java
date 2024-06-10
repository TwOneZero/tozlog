package com.tozlog.api;

import com.tozlog.api.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;


@ConfigurationPropertiesScan(basePackageClasses = {AppConfig.class})
@SpringBootApplication
public class TozlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(TozlogApplication.class, args);
    }

}
