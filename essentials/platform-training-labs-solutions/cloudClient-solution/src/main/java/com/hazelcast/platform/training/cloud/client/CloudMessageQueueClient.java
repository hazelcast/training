package com.hazelcast.platform.training.cloud.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.collection.IQueue;



/**
 * Cloud IMDG Exercise 2 - Messaging - sending & consuming messages using IMDG cloud service queue
 *
 */

public class CloudMessageQueueClient {
	
	private HazelcastInstance hzCloudClient;
	
	public CloudMessageQueueClient(){
	}
	
	private void init() {
		//TODO: Create a Hazelcast cloud client instance 
		hzCloudClient = HazelcastClient.newHazelcastClient(Utils.getCloudClientConfig());
		Thread messageProducerThread = new Thread(new Runnable(){
			public void run(){
				produceMessages();
			}
		
		}); 
		
		Thread messageConsumerThread = new Thread(new Runnable(){
			public void run(){
				consumeMessages();
			}
		
		}); 
		
        //start producing and consuming messages
		messageProducerThread.start();
		messageConsumerThread.start();
	}

	private void produceMessages() {
		try {
			//TODO: get reference handle to a named distributed Queue 
			IQueue<String> trainingQueue = hzCloudClient.getQueue("TrainingQueue");
			int i=0;
			 while(true){
				 trainingQueue.offer("Adding Message "+ (++i));
				Thread.sleep(500);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}				

	private void consumeMessages(){
		IQueue<String> trainingQueue = hzCloudClient.getQueue("TrainingQueue");
		while (true){
			try {
				String value = trainingQueue.take();
				System.out.println(" Message Consumed :"+value);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
		
	public static void main(String[] args) {
		CloudMessageQueueClient cloudMessageQueueClient = new CloudMessageQueueClient();
		cloudMessageQueueClient.init();
	}

 }
