package com.sacars;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.sacars.model")
@EnableJpaRepositories("com.sacars.repository")
public class SaCarsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaCarsApplication.class, args);
    }
}
