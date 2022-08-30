package com.qurlapi.qurlapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"com.qurlapi.qurlapi.model"})
@EnableJpaRepositories(basePackages = {"com.qurlapi.qurlapi.dao"})
@SpringBootApplication(scanBasePackages = {"com.qurlapi.qurlapi"})
public class QUrlApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(QUrlApiApplication.class, args);
    }

}
