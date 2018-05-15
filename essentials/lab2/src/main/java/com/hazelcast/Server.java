package com.hazelcast;

import com.hazelcast.core.Hazelcast;

public class Server {
    public static void main(String[] args) {
        // Starting a Hazelcast node, this time we want to use
        // a standalone cluster, therefore no reference needs
        // to be kept around
        Hazelcast.newHazelcastInstance();
    }
}
