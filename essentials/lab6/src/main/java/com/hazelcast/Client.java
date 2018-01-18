package com.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Client {
    public static void main(String[] args) throws Exception {
        // Create Hazelcast instance which is backed by a client
        HazelcastInstance client = HazelcastClient.newHazelcastClient();

        // Create a Hazelcast backed map
        Map<Integer, String> map = client.getMap("training");

        // Write the 1000000 elements to the map
        for (int i = 0; i < 1000000; i++) {
            int key = i;
            String value = "value-" + String.valueOf(i);

            // Put the entry into the map
            map.put(key, value);

            // Wait a bit to see changes in Management Center
            TimeUnit.MILLISECONDS.sleep(250);
        }
    }
}
