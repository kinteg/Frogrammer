package com.kinteg.frogrammer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FrogrammerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrogrammerApplication.class, args);
	}

}
