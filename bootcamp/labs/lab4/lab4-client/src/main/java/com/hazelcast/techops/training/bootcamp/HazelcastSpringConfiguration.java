package com.hazelcast.techops.training.bootcamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hazelcast.HazelcastKeyValueAdapter;
import org.springframework.data.hazelcast.repository.config.EnableHazelcastRepositories;
import org.springframework.data.keyvalue.core.KeyValueTemplate;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.core.HazelcastInstance;

/**
 * <P>The only mention of Hazelcast in the client code.
 * </P>
 * <P>Boilerplate code, may be derived in later versions.
 * </P>
 */
@Configuration
@EnableHazelcastRepositories
public class HazelcastSpringConfiguration {

	@Autowired
	private HazelcastInstance hazelcastInstance;

	@Bean
	public HazelcastInstance hazelcastInstance() throws Exception {
		ClientConfig clientConfig = new XmlClientConfigBuilder("hazelcast-client.xml").build();
		return HazelcastClient.newHazelcastClient(clientConfig);
	}
	
	@Bean
	public HazelcastKeyValueAdapter hazelcastKeyValueAdapter(HazelcastInstance hazelcastInstance) {
	    return new HazelcastKeyValueAdapter(this.hazelcastInstance);
	}

	@Bean
    public KeyValueTemplate keyValueTemplate(HazelcastKeyValueAdapter hazelcastKeyValueAdapter) {
		return new KeyValueTemplate(hazelcastKeyValueAdapter);
	}

}
