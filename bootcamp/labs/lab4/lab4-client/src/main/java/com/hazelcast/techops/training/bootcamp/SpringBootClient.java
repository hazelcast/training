package com.hazelcast.techops.training.bootcamp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringBootClient {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(SpringBootClient.class, args);
		
		CapitalService capitalService = applicationContext.getBean(CapitalService.class);
		
		System.out.println("------- Population > 10 Million");
		capitalService.tenMillionPlus().forEach(capital -> {
			System.out.println(capital);
		});
		System.out.println("=======");
		
		System.out.println("------- Names in order");
		capitalService.namesDescending().forEach(capital -> {
			System.out.println(capital.getName());
		});

		System.out.println("=======");

		System.exit(0);
	}
	
}
