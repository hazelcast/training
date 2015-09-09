package com.hazelcast.training.compute.lab9;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;

import java.util.Date;

public class PublisherMember {
    public static void main(String[] args) {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient();
        ITopic<Date> topic = hz.getTopic("topic");
        topic.publish(new Date());
    }
}
