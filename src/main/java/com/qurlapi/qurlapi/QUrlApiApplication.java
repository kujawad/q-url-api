package com.qurlapi.qurlapi;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.core.annotation.SynthesizedAnnotation;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.nativex.hint.JdkProxyHint;

@JdkProxyHint(types = {Tag.class, SynthesizedAnnotation.class})
@EntityScan(basePackages = {"com.qurlapi.qurlapi.model"})
@EnableJpaRepositories(basePackages = {"com.qurlapi.qurlapi.dao"})
@SpringBootApplication(scanBasePackages = {"com.qurlapi.qurlapi"})
public class QUrlApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(QUrlApiApplication.class, args);
    }
}
