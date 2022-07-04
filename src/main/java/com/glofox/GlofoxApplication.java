package com.glofox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class GlofoxApplication {

	public static void main(String[] args) {
		SpringApplication.run(GlofoxApplication.class, args);
	}

}
