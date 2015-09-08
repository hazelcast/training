package com.hazelcast.training.compute.lab3;

import com.hazelcast.core.Hazelcast;

public class SlaveMember {
    public static void main(String[] args) {
        Hazelcast.newHazelcastInstance();
    }
}
