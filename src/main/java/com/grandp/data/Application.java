package com.grandp.data;

import com.grandp.data.entity.enumerated.Semester;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

@SpringBootApplication
@EnableAutoConfiguration
public class Application {


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
