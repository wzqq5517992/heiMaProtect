package com.itcast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class OrderProviderNacosApplication {

	public static void main(String[] args) {
	    SpringApplication.run(OrderProviderNacosApplication.class, args);
	}

}