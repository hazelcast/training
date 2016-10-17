package com.hazelcast.techops.training.bootcamp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;

import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

/**
 * <P>Create a Hazelcast server instance.
 * </P>
 * <P>In client-server mode we don't need the server command
 * line processor to do much, as most of the processing done
 * on the server is triggered by the client rather than
 * the command line.
 * </P>
 */
public class MyServer {

	private static final String PREFIX = " -> ";
	
	/* List of commands this process accepts from the console.
	 */
	private enum Command {
        LIST, QUIT
	}

	private static HazelcastInstance hazelcastInstance;

	/*  Create a Hazelcast instance, do some processing, then shut that
	 * instance down.
	 */
	public static void main(String[] args) throws Exception {
		
		System.setProperty("hazelcast.logging.type", "slf4j");

		// Use 'hazelcast.xml' by default
		MyServer.hazelcastInstance = Hazelcast.newHazelcastInstance();

		MyServer.process();

		MyServer.hazelcastInstance.shutdown();
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

							case LIST:
								MyServer.commandList();
								break;

							case QUIT:
								return;

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
	
	/* List the objects currently defined to the Hazelcast
	 * cluster.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void commandList() {
		for (DistributedObject distributedObject : MyServer.hazelcastInstance.getDistributedObjects()) {
			
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

				System.out.println(distributedObject.getClass().getSimpleName() + " '"
						+ distributedObject.getName() + "'");

			}
		}
	}
	
}
