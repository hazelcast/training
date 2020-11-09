package com.hazelcast.platform.training.imdg.client;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Random;

import com.hazelcast.aggregation.Aggregators;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

/**
 * IMDG Cluster exercise 2 - Demonstrate distribute map using C/S deployment topology
 *
 */

public class MapClient {
	
	HazelcastInstance instance;
	
	public MapClient() throws FileNotFoundException{

		//TODO: Create a Hazelcast client instance to the the running 'PRIMARY' cluster
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.setClusterName("PRIMARY");
		clientConfig.getNetworkConfig().addAddress("localhost:6701");
		instance =  HazelcastClient.newHazelcastClient(clientConfig);

		Thread mapWriterThread = new Thread(new Runnable(){
			public void run(){
				doMapPuts();
			}
		}); 
		
		mapWriterThread.start();
		
	}
	
	private void doMapPuts(){
		try {
			
			//TODO: Get a reference handle to the distribute map
			IMap<Object,Object> trainnigMap = instance.getMap("TrainnigMap");

			Random randInt = new Random();
			 while(true){

				String key = "key"+Integer.toString(randInt.nextInt(10000));
				String value = Double.toString(Math.random());
				System.out.println("Adding Entry key: "+ key);

				//TODO: Put entry into the map				
				trainnigMap.set(key,value);
		        //String OldValue = trainnigMap.put(key,value);
		        
		        getMapEntryCount(trainnigMap);
		        
				Thread.sleep(500);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	private static long getMapEntryCount(IMap<Object, Object> map) {
        //Execute the aggregation compute and print the result
		//NOTE on a distributed map, this is recommended instead of map.size()
        long entryCount = map.aggregate(Aggregators.<Map.Entry<Object, Object>>count());
        System.out.println("Number of entries in Map<" +map.getName()+">: " + entryCount);
        return entryCount;
	}

	public static void main(String[] args) {
		try {
			MapClient mapClient = new MapClient();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
 }
