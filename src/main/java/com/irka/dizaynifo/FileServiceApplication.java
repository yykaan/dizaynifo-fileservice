package com.irka.dizaynifo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"com.irka.infrastructure", "com.irka.dizaynifo", "com.irka.common"})
@EntityScan({"com.irka.infrastructure", "com.irka.dizaynifo", "com.irka.common"})
@EnableJpaRepositories(basePackages = {"com.irka.infrastructure", "com.irka.dizaynifo", "com.irka.common"})
@EnableMongoRepositories(basePackages = "com.irka.dizaynifo")
public class FileServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileServiceApplication.class, args);
    }

}