package com.hazelcast;

import java.util.Map;

public class Client2 {
    public static void main(String[] args) {
        // Create Hazelcast instance which is backed by a client


        // Create a Hazelcast backed map
        Map<Integer, String> map = ;

        // Get key 42 from the map and store the value
        String result = map.get(42);

        // Print the result to the console
        System.out.println(result);
    }
}
