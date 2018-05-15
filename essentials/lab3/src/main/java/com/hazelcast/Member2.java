package com.hazelcast;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class Member2 {
    public static void main(String[] args) {
        // Starting a Hazelcast node
        HazelcastInstance node = Hazelcast.newHazelcastInstance();

        // Create a Hazelcast backed map


        // Get key 42 from the map and store the value
        String result = null;//remove null

        // Print the result to the console
        System.out.println(result);
    }
}
