package com.hazelcast.techops.training.bootcamp;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.ManagementCenterConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import lombok.extern.slf4j.Slf4j;

/**
 * <P>Annotation style configuration, three instances in the one JVM.
 * </P>
 */
@Configuration
@Slf4j
public class HazelcastSpringConfiguration {

	@Bean
	public GroupConfig groupConfig(
			@Value("${spring.application.name}") String springApplicationName
			) {
		log.error("GROUPR CONFIG");//XXX
		
		GroupConfig groupConfig = new GroupConfig();
		
		groupConfig.setName(springApplicationName);
		groupConfig.setPassword(System.getProperty("user.name"));
		
		log.info(groupConfig.toString());
		
		return groupConfig;
	}

	@Bean
	public ManagementCenterConfig managementCenterConfig() {
		return new ManagementCenterConfig()
				.setEnabled(true)
				.setUrl("http://localhost:8080/mancenter");
	}

	@Bean
	public Config config(GroupConfig groupConfig, ManagementCenterConfig managementCenterConfig) {
		log.error("CONFIG");//XXX
		Config config = new Config();
		
		config.setGroupConfig(groupConfig);
		
		config.setManagementCenterConfig(managementCenterConfig);
		
		NetworkConfig networkConfig = config.getNetworkConfig();
		networkConfig.setPort(5701);
		networkConfig.setPortAutoIncrement(true);
		networkConfig.setPortCount(3);
		
		networkConfig.getJoin().getMulticastConfig().setEnabled(false);
		
		networkConfig.getJoin().getTcpIpConfig().setEnabled(true);
		networkConfig.getJoin().getTcpIpConfig().setMembers(Arrays.asList(new String[]{"127.0.0.1","127.0.0.1:5702"}));
		
		return config;
	}

	
	@Bean
	public HazelcastInstance hazelcastInstance1(Config config) {
		return Hazelcast.newHazelcastInstance(config);
	}
	
	@Bean
	public HazelcastInstance hazelcastInstance2(Config config) {
		return Hazelcast.newHazelcastInstance(config);
	}
	
	@Bean
	public HazelcastInstance hazelcastInstance3(Config config) {
		return Hazelcast.newHazelcastInstance(config);
	}
}
