package com.hazelcast.platform.training.common;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

public class Utils {

	public  static ClientConfig clientConfigForExternalHazelcast() {
        ClientConfig cfg = new ClientConfig();
        cfg.getNetworkConfig().addAddress("localhost:6701");
        cfg.setClusterName("PRIMARY");
        return cfg;
    }
	
	public static HazelcastInstance remoteHazelcastInstance(ClientConfig clientConfig){
		return HazelcastClient.newHazelcastClient(clientConfig);
	}
}
