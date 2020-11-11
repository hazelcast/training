package com.hazelcast.platform.training.cloud.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

/**
 * Cloud IMDG Exercise 1 - Simply putting entries into hosted IMDG cloud service
 *
 */
public class CloudMapClient {
	private static HazelcastInstance hzCloudClient;
	
	public CloudMapClient(){
		CloudMapClient.init();
	}
	
	private static void init() {
		if(hzCloudClient == null) {
			hzCloudClient = HazelcastClient.newHazelcastClient(Utils.getCloudClientConfig());
		}
	}
	
	public void putEntriesIntoCloudMap(){
		try {
			
			IMap<String,String> trainingMap = hzCloudClient.getMap("TrainingMap");
			int i=0;
			 while(true){
				System.out.println("Adding Entry "+ (++i));
				String key = "key" + Integer.toString(i);
				String value = Double.toString(Math.random());
				//TODO: Extra credit -- what is the difference between put and set?
		        //String OldValue = map.put(Integer.toString(i),Double.toString(Math.random()));
				trainingMap.set(key,value);
				Thread.sleep(500);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		CloudMapClient cloudMapClient = new CloudMapClient();
		cloudMapClient.putEntriesIntoCloudMap();
	}
 }
