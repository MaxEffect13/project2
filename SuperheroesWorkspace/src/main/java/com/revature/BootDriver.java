package com.revature;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EntityScan(basePackages = {"com.revature.models"} )
//@EnableJpaRepositories(basePackages = {"com.revature.repositories"})
public class BootDriver {
	public static void main(String[] args) {
		SpringApplication.run(BootDriver.class, args);
	
		
	}
}
