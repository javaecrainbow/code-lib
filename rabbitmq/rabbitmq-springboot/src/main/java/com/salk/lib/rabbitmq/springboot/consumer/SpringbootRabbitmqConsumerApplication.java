package com.salk.lib.rabbitmq.springboot.consumer;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SpringbootRabbitmqConsumerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringbootRabbitmqConsumerApplication.class)
            .properties("spring.config.name:application-consumer").build().run(args);
    }

}
