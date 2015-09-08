package com.hazelcast.training.compute.lab5;

import com.hazelcast.core.Hazelcast;

public class SlaveMember {
    public static void main(String[] args) {
        Hazelcast.newHazelcastInstance();
    }
}
