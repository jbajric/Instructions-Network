package com.instructionnetwork.rezervacije;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class RezervacijeApplication {

	public static void main(String[] args) {
		SpringApplication.run(RezervacijeApplication.class, args);
	}

}
