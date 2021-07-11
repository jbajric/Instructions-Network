package com.instructionnetwork.raspored;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class FreeTimeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FreeTimeApplication.class, args);
	}

}
