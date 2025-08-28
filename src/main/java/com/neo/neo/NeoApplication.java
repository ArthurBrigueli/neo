package com.neo.neo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NeoApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeoApplication.class, args);
	}

}
