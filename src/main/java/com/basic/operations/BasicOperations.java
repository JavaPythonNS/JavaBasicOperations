package com.basic.operations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BasicOperations {

	public static void main(String[] args) {
		SpringApplication.run(BasicOperations.class, args);
	}
}
