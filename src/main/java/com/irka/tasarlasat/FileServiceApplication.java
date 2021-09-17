package com.irka.tasarlasat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"com.irka.infrastructure", "com.irka.tasarlasat", "com.irka.common"})
@EntityScan({"com.irka.infrastructure", "com.irka.tasarlasat", "com.irka.common"})
@EnableJpaRepositories(basePackages = {"com.irka.infrastructure", "com.irka.tasarlasat", "com.irka.common"})
public class FileServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileServiceApplication.class, args);
    }

}