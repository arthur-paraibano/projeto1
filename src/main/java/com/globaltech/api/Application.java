package com.globaltech.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})
@SpringBootApplication(scanBasePackages = "com.globaltech.api")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
