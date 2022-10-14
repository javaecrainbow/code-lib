package com.salk.lib.rabbitmq.springboot.producer;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author salkli
 * @since 2022/7/26
 **/
@SpringBootApplication
public class SpringbootRabbitmqProducerApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringbootRabbitmqProducerApplication.class)
                .properties("spring.config.name:application-producer").build().run(args);
    }
}
