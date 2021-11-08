package com.salk.lib.practice.delaycheck.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
public class MqReliabledeliveryDelayChechOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(MqReliabledeliveryDelayChechOrderApplication.class, args);
	}
}
