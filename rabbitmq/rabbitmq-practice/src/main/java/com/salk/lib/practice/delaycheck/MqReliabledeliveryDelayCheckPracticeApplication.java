package com.salk.lib.practice.delaycheck;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author salk
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
public class MqReliabledeliveryDelayCheckPracticeApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(MqReliabledeliveryDelayCheckPracticeApplication.class)
				.properties("spring.config.name:application-producer").build().run(args);
		new SpringApplicationBuilder(MqReliabledeliveryDelayCheckPracticeApplication.class)
				.properties("spring.config.name:application-consumer").build().run(args);
		new SpringApplicationBuilder(MqReliabledeliveryDelayCheckPracticeApplication.class)
				.properties("spring.config.name:application-callback").build().run(args);
	}
}
