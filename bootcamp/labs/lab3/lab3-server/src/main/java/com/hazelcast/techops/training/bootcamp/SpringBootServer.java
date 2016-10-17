package com.hazelcast.techops.training.bootcamp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <P>Start Spring. Component scanning will find {@link HazelcastSpringConfiguration}
 * and create some beans.
 * </P>
 */
@SpringBootApplication
public class SpringBootServer {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootServer.class, args);
	}

}
