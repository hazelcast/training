package com.hazelcast.techops.training.bootcamp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;

import com.hazelcast.config.ClasspathXmlConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.ExecutionCallback;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;
import com.hazelcast.core.IMap;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;

import lombok.extern.slf4j.Slf4j;

/**
 * <P>Create a Hazelcast server instance.
 * </P>
 * 
 * <P>Use command line argument to dictate how this is configured:
 * </P>
 * <OL>
 * <LI><B>none</B> No config, pick up the default, using multicast discovery on port 5701.
 * </LI>
 * <LI><B>java</B> Java config, specified in code, here uses TCP discovery on localhost, port 5701 upwards.
 * </LI>
 * <LI><B>xml</B> XML config from {@code hazelcast.xml} file, uses TCP discovery on localhost, port 5701 upwards.
 * </LI>
 * <LI><B>standalone</B> Java config again, specified in code, turns off networking so can't find other servers.
 * </LI>
 * </OL>
 * 
 * <P>
 * Use a rudimentary command line processor to manipulate the Hazelcast cluster.
 * </P>
 * 
 * <P>
 * At the end, when exiting, shutdown the Hazelcast instance so that any hosted data can be offloaded.
 * </P>
 * 
 * <P>
 * This is a rather large class, and there are plenty utilities to do command line processing.
 * </P>
 */
@Slf4j
public class MyServer {

	private static final String[] MAP_NAMES = { "mapWithBackup", "mapWithoutBackup" };
	private static final String PREFIX = " -> ";
	private static final String USERNAME = System.getProperty("user.name");
	
	/* List of commands this process accepts from the console.
	 */
	private enum Command {
        ATOMIC_INCREMENT, ATOMIC_READ, ATOMIC_WRITE, INIT, LOAD_MAPS, LIST, MAP_READ, MAP_WRITE,
        QUEUE_READ, QUEUE_WRITE, QUIT, TOPIC_READ, TOPIC_WRITE
	}

	private static HazelcastInstance hazelcastInstance;

	/*  Create a Hazelcast instance, do some processing, then shut that
	 * instance down.
	 *
	 */
	public static void main(String[] args) throws Exception {
		
		System.setProperty("hazelcast.logging.type", "slf4j");

		if ((MyServer.hazelcastInstance = MyServer.getHazelcast(args))!=null) {
			MyServer.process();
			MyServer.hazelcastInstance.shutdown();
		}
	}

	/* Command line processor, expecting one of the enumerated
	 * commands plus any arguments appropriate to each.
	 */
	private static void process() throws Exception {

		try (InputStreamReader inputStreamReader = new InputStreamReader(System.in);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) {

			MyServer.banner();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				String[] tokens = line.toLowerCase().split(" ");
				if (tokens[0].length() > 0) {
					try {
						Command command = Command.valueOf(tokens[0].toUpperCase());
						System.out.println("> " + command);

						try {

							switch (command) {
							
							case ATOMIC_INCREMENT:
								MyServer.commandAtomicIncrement();
								break;
							
							case ATOMIC_READ:
								MyServer.commandAtomicRead();
								break;
							
							case ATOMIC_WRITE:
								if (MyServer.hasArgs(command, tokens, 1)) {
									MyServer.commandAtomicWrite(tokens[1]);
								}
								break;
							
							case INIT:
								MyServer.commandInit();
								break;
							
							case LIST:
								MyServer.commandList();
								break;
								
							case LOAD_MAPS:
								MyServer.commandLoadMaps();
								break;
								
							case MAP_READ:
								if (MyServer.hasArgs(command, tokens, 1)) {
									MyServer.commandMapRead(tokens[1]);
								}
								break;

							case MAP_WRITE:
								if (MyServer.hasArgs(command, tokens, 2)) {
									MyServer.commandMapWrite(tokens[1], tokens[2]);
								}
								break;
								
							case QUEUE_READ:
								MyServer.commandQueueRead();
								break;
								
							case QUEUE_WRITE:
								if (MyServer.hasArgs(command, tokens, 1)) {
									MyServer.commandQueueWrite(tokens[1]);
								}
								break;

							case QUIT:
								return;

							case TOPIC_READ:
								MyServer.commandTopicRead();
								break;

							case TOPIC_WRITE:
								if (MyServer.hasArgs(command, tokens, 1)) {
									MyServer.commandTopicWrite(tokens[1]);
								}
								break;
								
							}
							
						} catch (Exception exceptionInvokingCommand) {
							exceptionInvokingCommand.printStackTrace(System.err);
						}

					} catch (IllegalArgumentException exceptionParsingCommand) {
						System.out.println("'" + line + "' unrecognised");
					}

					MyServer.banner();
				}
			}
		}
		
	}

	private static void usage() {
		System.err.println(MyServer.class.getSimpleName() + ": usage: " + MyServer.class.getSimpleName() +
				" none|xml|java|standalone");
	}

	private static void banner() {
		String commands = Arrays.asList(Command.values()).toString();

		for (int i=0 ; i<commands.length(); i++) {
			System.out.print("=");
		}
		System.out.println("");

		System.out.println(commands);

		for (int i=0 ; i<commands.length(); i++) {
			System.out.print("=");
		}
		System.out.println("");
	}
	
	private static boolean hasArgs(Command command, String[] string, int count) {
        if ((string.length-1) != count) {
            System.out.printf("'%s' needs %d arg%s%n", command, count, (count==1 ? "" : "s"));
            return false;

        }

        for (int i = 1 ; i <= count ; i++) {
            if (string[i].trim().length() == 0) {
                    System.out.printf("Arg %d cannot be empty", i);
                    return false;
            }
        }

        return true;
    }

	/* Expect one command line argument, such as "java -jar MyServer standalone"
	 * to specify standalone mode.
	 * 
	 * Build a Config object in response to the command line request, then
	 * use that to spawn a Hazelcast server.
	 */
	private static HazelcastInstance getHazelcast(String[] args) {
		Config config = null;
		JoinConfig joinConfig = null;
		
		if (args.length!=1) {
			MyServer.usage();
		} else {
			switch (args[0].toLowerCase()) {
			case "java":
				log.info("Using Java config, TCP based");
				config = new Config();
				joinConfig = config.getNetworkConfig().getJoin();
				joinConfig.getMulticastConfig().setEnabled(false);
				joinConfig.getTcpIpConfig().setEnabled(true);
				joinConfig.getTcpIpConfig().setMembers(Arrays.asList("127.0.0.1"));
				break;
			case "none":
				log.info("Using empty config, defaults to multi-cast");
				config = new Config();
				break;
			case "standalone":
				log.info("Using standalone config, no networking");
				config = new Config();
				joinConfig = config.getNetworkConfig().getJoin();
				joinConfig.getMulticastConfig().setEnabled(false);
				joinConfig.getTcpIpConfig().setEnabled(false);
				break;
			case "xml":
				String xmlFilename = "hazelcast.xml";
				log.info("Using config from '{}'", xmlFilename);
				config = new ClasspathXmlConfig(xmlFilename);
				break;
			default:	
				MyServer.usage();
			}
		}

		if (config!=null) {
			return Hazelcast.newHazelcastInstance(config);
		} else {
			return null;
		}
	}

	/* Increment an atomic. This might not be stored on this JVM
	 * but will appear to be, and atomically update.
	 * For fun, do this asynchronously.
	 */
	private static void commandAtomicIncrement() {
		IAtomicLong iAtomicLong = MyServer.hazelcastInstance.getAtomicLong(USERNAME);
		
		iAtomicLong.incrementAndGetAsync().andThen(
				new ExecutionCallback<Long>() {
					public void onFailure(Throwable arg0) {
						arg0.printStackTrace(System.err);
					}

					public void onResponse(Long arg0) {
						System.out.println(PREFIX + "Updated value of atomic '" + iAtomicLong.getName() + "' to " + arg0 + ".");
					}
			     }
		);
		
	}

	private static void commandAtomicRead() {
		IAtomicLong iAtomicLong = MyServer.hazelcastInstance.getAtomicLong(USERNAME);
		
		long current = iAtomicLong.get();
		
		System.out.println(PREFIX + "Current value of atomic '" + iAtomicLong.getName() + "' is " + current + ".");
	}

	private static void commandAtomicWrite(String arg0) throws Exception {
		long newValue = Long.parseLong(arg0);
		
		IAtomicLong iAtomicLong = MyServer.hazelcastInstance.getAtomicLong(USERNAME);
		
		iAtomicLong.set(newValue);

		System.out.println(PREFIX + "Updated value of atomic '" + iAtomicLong.getName() + "' to " + arg0 + ".");
	}

	/* Force create some distributed objects on the available
	 * JVMs.
	 */
	private static void commandInit() {
		for (String mapName : MAP_NAMES) {
			System.out.println(PREFIX + "Access '" + mapName + "'");
			MyServer.hazelcastInstance.getMap(mapName);
		}
	}
	
	/* List the objects currently defined to the Hazelcast
	 * cluster.
	 * Expecting an IAtomicLong, IMap, IQueue or ITopic.
     * Names may be repeated if types differ.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void commandList() {
		for (DistributedObject distributedObject : MyServer.hazelcastInstance.getDistributedObjects()) {
			
			if (distributedObject instanceof IAtomicLong) {

				IAtomicLong iAtomicLong = (IAtomicLong) distributedObject;
				System.out.println("IAtomicLong '" + iAtomicLong.getName() + "'");
				
				System.out.println(PREFIX + "value==" + iAtomicLong.get());

			} else {
				if (distributedObject instanceof IMap) {
					
					IMap iMap = (IMap) distributedObject;
					System.out.println("IMap '" + iMap.getName() + "'");
					
					System.out.println(PREFIX + "size==" + iMap.size());
					// Try to print in key order
					try {
						iMap
							.entrySet()
								.stream()
									.sorted(Map.Entry.comparingByKey())
										.forEach(entry -> {
											System.out.println(PREFIX + PREFIX + entry);
										});
					} catch (ClassCastException classCastException) {
						System.out.println(PREFIX + PREFIX + classCastException.getMessage());
					}
					
				} else {
					if (distributedObject instanceof IQueue) {

						IQueue iQueue = (IQueue) distributedObject;
						System.out.println("IQueue '" + iQueue.getName() + "'");
						
						System.out.println(PREFIX + "size==" + iQueue.size());

					} else {
						if (distributedObject instanceof ITopic) {

							ITopic iTopic = (ITopic) distributedObject;
							System.out.println("ITopic '" + iTopic.getName() + "'");
							
						} else {

							System.out.println(distributedObject.getClass().getSimpleName() + " '"
									+ distributedObject.getName() + "'");

						}
					}
				}
			}
		}
	}

	/* Put some Java system properties into a map as sample data
	 */
	private static void commandLoadMaps() {
		for (String mapName : MAP_NAMES) {
			IMap<String, String> iMap = MyServer.hazelcastInstance.getMap(mapName);
			
			System.getProperties()
				.entrySet()
					.stream()
						.filter(property -> property.getKey().toString().startsWith("java"))
							.forEach(property -> {
								String key = property.getKey().toString();
								String value = property.getValue().toString();
								
								log.info(PREFIX + "Put " + key + "==" + value + " to '" + iMap.getName() + "'");
								iMap.put(key, value);
							});			
		}
	}

	/* Access a Hazelcast IMap. Appears to be like a Map entirely on this
	 * server but will really be sharded across all servers.
	 */
	private static void commandMapRead(String key) throws Exception {
		IMap<String, String> iMap = MyServer.hazelcastInstance.getMap(USERNAME);

		String value = iMap.get(key);
		
		if (value==null) {
			System.out.println(PREFIX + "Map '" + iMap.getName() + "' did not contain key '" + key + "'.");
		} else {
			System.out.println(PREFIX + "Map '" + iMap.getName() + "' had key '" + key + "'" +
					" with value '" + value + "'");
		}
	}
	
	private static void commandMapWrite(String key, String newValue) throws Exception {
		IMap<String, String> iMap = MyServer.hazelcastInstance.getMap(USERNAME);

		// iMap.set(key) can be more efficient
		String oldValue = iMap.put(key, newValue);
		
		if (oldValue==null) {
			System.out.println(PREFIX + "Map '" + iMap.getName() + "', created key '" + key + "'" +
					" with value '" + newValue + "'");
		} else {
			System.out.println(PREFIX + "Map '" + iMap.getName() + "', changed key '" + key + "'" +
					" from '" + oldValue + "' to '" + newValue + ".");
		}
	}

	/* Read and remove an item from a queue hosted somewhere
	 * on the cluster.
	 */
	private static void commandQueueRead() {
		IQueue<String> iQueue = MyServer.hazelcastInstance.getQueue(USERNAME);

		String head = iQueue.poll();
		
		if (head==null) {
			System.out.println(PREFIX + "Queue '" + iQueue.getName() + "' was empty.");
		} else {
			System.out.println(PREFIX + "Queue '" + iQueue.getName() + "' read '" + head + "'.");
		}
	}

	private static void commandQueueWrite(String arg0) throws Exception {
		IQueue<String> iQueue = MyServer.hazelcastInstance.getQueue(USERNAME);

		iQueue.add(arg0);
		
		int size = iQueue.size();

		System.out.println(PREFIX + "Queue '" + iQueue.getName() + "' wrote '" + arg0 + "' taking size to " + size + ".");
	}

	/* Add a listener on a topic. When one cluster member publishes
	 * all subscribers get it, which can include the publisher.
	 * 
	 * Run this twice, add two message listeners, one output from each.
	 */
	private static void commandTopicRead() {
		ITopic<String> iTopic = MyServer.hazelcastInstance.getTopic(USERNAME);

		iTopic.addMessageListener(new MessageListener<String>() {

			public void onMessage(Message<String> message) {
				System.out.println(PREFIX + "Topic '" + iTopic.getName() + "' read '" + message.getMessageObject() + "' by " + this);
			}
			
		});
		
		System.out.println(PREFIX + "Now subscribed to Topic '" + iTopic.getName() + "'");
	}

	private static void commandTopicWrite(String arg0) throws Exception {
		ITopic<String> iTopic = MyServer.hazelcastInstance.getTopic(USERNAME);

		iTopic.publish(arg0);

		System.out.println(PREFIX + "Topic '" + iTopic.getName() + "' wrote '" + arg0 + "'.");
	}

}
