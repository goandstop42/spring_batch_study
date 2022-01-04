package com.example.springbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jdk.jfr.Enabled;

@EnableBatchProcessing
@SpringBootApplication
public class SpringBatchApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(SpringBatchApplication.class, args);
	}

}
