package com.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;

import java.util.Map;

public class OnlyReadingClient {
    // This client only reads values, that should work with read-only permissions
    public static void main(String[] args) {
        // Create Hazelcast instance which is backed by a client
        HazelcastInstance client = HazelcastClient.newHazelcastClient();

        // Create a Hazelcast backed map
        Map<Integer, String> map = client.getMap("training");

        // Write the 1000 elements to the map
        for (int i = 0; i < 1000; i++) {
            // Read value from map
            System.out.println("Value of " + i + ":" + map.get(i));
        }
    }
}
