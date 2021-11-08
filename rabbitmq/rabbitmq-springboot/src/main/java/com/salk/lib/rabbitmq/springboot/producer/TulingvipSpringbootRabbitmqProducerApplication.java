package com.salk.lib.rabbitmq.springboot.producer;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
class SpringbootRabbitmqProducerApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(SpringbootRabbitmqProducerApplication.class)
				.properties("spring.config.name:application-producer").build().run(args);
	}

}
