package com.salk.lib.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author salkli
 * @date 2022/2/23
 */
@SpringBootApplication
public class SpringModuleApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(SpringModuleApplication.class);
        springApplication.run(args);
    }

}
