package com.nsunf.newsvoca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NewsvocaApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsvocaApplication.class, args);
	}

}
