package com.hazelcast.platform.training.jet.client;

import  com.hazelcast.platform.training.common.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.collection.IQueue;
import com.hazelcast.core.HazelcastInstance;

public class InteractiveTextMessageGenerator {
	HazelcastInstance instance;
	public InteractiveTextMessageGenerator(){
		 instance = HazelcastClient.newHazelcastClient(Utils.clientConfigForExternalHazelcast());
		Thread producer = new Thread(new Runnable(){
			public void run(){
				processInput();
			}
		}); producer.start();
		
	    
	}
	
	private void processInput(){
		try {
			
			IQueue<String> queue=instance.getQueue("TrainingSourceQueue");
			BufferedReader reader =
	                   new BufferedReader(new InputStreamReader(System.in));
			while(true){
				System.out.println("Enter Text");
		        String value = reader.readLine();
				queue.put(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		InteractiveTextMessageGenerator interactiveTextMessageGenerator = new InteractiveTextMessageGenerator();
	}
    

}
