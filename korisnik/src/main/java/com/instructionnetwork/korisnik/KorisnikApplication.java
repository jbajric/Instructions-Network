package com.instructionnetwork.korisnik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class KorisnikApplication {

	public static void main(String[] args) {
		SpringApplication.run(KorisnikApplication.class, args);
	}

}
