package com.hazelcast.techops.training.bootcamp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.IMap;
import com.hazelcast.durableexecutor.DurableExecutorService;
import com.hazelcast.durableexecutor.DurableExecutorServiceFuture;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.SqlPredicate;

/**
 * <P>Create a Hazelcast client instance.
 * </P>
 * <P>Rather than retrieve the data to process, in most
 * cases this client invokes processing that runs on
 * the servers.
 * </P>
 * <P>
 * <OL>
 * <LI><B>CLOSE_QUOTES</B>
 * Find quotes that can be converted into orders.
 * </LI>
 * <LI><B>LOAD_TESDATA</B>
 * Ask a server to create test data.
 * </LI>
 * <LI><B>SQL</B>
 * Run a query against the data.
 * </LI>
 * <LI><B>TASK_LAUNCH</B>
 * Submit a slow running task to get results later.
 * </LI>
 * <LI><B>TASK_RETRIEVE</B>
 * Get the result of the slow running task.
 * </LI>
 * <LI><B>TOUCH_QUOTES</B>
 * Change the last update time on some quotes.
 * </LI>
 * </OL>
 * </P>
 */
public class MyClient {

	private static final String PREFIX = " -> ";
	
	/* List of commands this process accepts from the console.
	 */
	private enum Command {
        CLOSE_QUOTES, LIST_MAPS, LOAD_TESTDATA, QUIT, SQL, TASK_LAUNCH, TASK_RETRIEVE, TOUCH_QUOTES
	}

	private static HazelcastInstance hazelcastInstance;

	/*  Create a Hazelcast client, do some processing, then shut it down.
	 */
	public static void main(String[] args) throws Exception {
		
		System.setProperty("hazelcast.logging.type", "slf4j");

		ClientConfig clientConfig = new XmlClientConfigBuilder("hazelcast-client.xml").build();
		MyClient.hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);

		MyClient.process();
		
		MyClient.hazelcastInstance.shutdown();
	}

	/* Command line processor, expecting one of the enumerated
	 * commands plus any arguments appropriate to each.
	 */
	private static void process() throws Exception {

		try (InputStreamReader inputStreamReader = new InputStreamReader(System.in);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) {

			MyClient.banner();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				String[] tokens = line.toLowerCase().split(" ");
				if (tokens[0].length() > 0) {
					try {
						Command command = Command.valueOf(tokens[0].toUpperCase());
						System.out.println("> " + command);

						try {

							switch (command) {

							case CLOSE_QUOTES:
								MyClient.commandCloseQuotes();
								break;

							case LIST_MAPS:
								MyClient.commandListMaps();
								break;

							case LOAD_TESTDATA:
								MyClient.commandLoadTestdata();
								break;

							case QUIT:
								return;

							case SQL:
								MyClient.commandSql();
								break;

							case TASK_LAUNCH:
								if (MyClient.hasArgs(command, tokens, 1)) {
									MyClient.commandTaskLaunch(tokens[1]);
								}
								break;

							case TASK_RETRIEVE:
								if (MyClient.hasArgs(command, tokens, 1)) {
									MyClient.commandTaskRetrieve(tokens[1]);
								}
								break;

							case TOUCH_QUOTES:
								MyClient.commandTouchQuotes();
								break;

							}
							
						} catch (Exception exceptionInvokingCommand) {
							exceptionInvokingCommand.printStackTrace(System.err);
						}

					} catch (IllegalArgumentException exceptionParsingCommand) {
						System.out.println("'" + line + "' unrecognised");
					}

					MyClient.banner();
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
	
	/* Turn open quotes into orders, if stock is available.
	 * Try running the SQL option before and after.
	 */
	private static void commandCloseQuotes() {
		IExecutorService iExecutorService = MyClient.hazelcastInstance.getExecutorService("default");
		
		MyOrderingRunnable myOrderingRunnable = new MyOrderingRunnable();
		
		iExecutorService.executeOnAllMembers(myOrderingRunnable);
		
		System.out.println(PREFIX + "Runnable has been invoked.");
	}
	
	/* List the maps that hold data.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void commandListMaps() {
		
		String[] mapNames = { Constants.ORDER_MAP_NAME, Constants.QUOTE_MAP_NAME, Constants.STOCK_MAP_NAME };
		
		for (String mapName : mapNames) {
			IMap iMap = MyClient.hazelcastInstance.getMap(mapName);
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
		}
		
	}

	/* Submit a java runnable to run on the cluster. The runnable
	 * injects test data from the server side. All the client
	 * does is request this be run.
	 */
	private static void commandLoadTestdata() throws Exception {
		ExecutorService executorService = MyClient.hazelcastInstance.getExecutorService("default");
		
		MyLoadingRunnable myLoadingRunnable = new MyLoadingRunnable();
		
		executorService.submit(myLoadingRunnable).get();
		
		System.out.println(PREFIX + "Runnable has run.");
	}

	/* Run a query predicate, results are returned to the client.
	 */
	@SuppressWarnings("unchecked")
	private static void commandSql() {
		String mapName = "lab2" + Quote.class.getSimpleName();
		IMap<Integer, Quote> quoteMap = MyClient.hazelcastInstance.getMap(mapName);
		
		String sql = "quantity > 20 AND open";
		
		System.out.println(PREFIX + "SQL: '" + sql + "'");
		
		Predicate<Integer, Quote> predicate = new SqlPredicate(sql);
		
		Set<Entry<Integer, Quote>> resultSet = quoteMap.entrySet(predicate);
		
		for (Entry<Integer, Quote> entry : resultSet) {
			System.out.println(PREFIX + PREFIX + entry);
		}
		
		System.out.println(PREFIX + "[" + resultSet.size() + " row" +
				(resultSet.size()==1 ? "" : "s") + "]");
	}

	/* Submit a callable that runs for a while and
	 * leaves a result to collect. This is a durable
	 * task, it will be re-run if a server dies while
	 * it is being run.
	 */
	private static void commandTaskLaunch(String arg0) throws Exception {
		long duration = Long.valueOf(arg0);
		
		DurableExecutorService durableExecutorService
			= MyClient.hazelcastInstance.getDurableExecutorService("lab2");
		
		MySleepyCallable myCallable = new MySleepyCallable(duration);
		
		DurableExecutorServiceFuture<String> future =
				durableExecutorService.submit(myCallable);

		long taskId = future.getTaskId();
		
		System.out.println(PREFIX + "Task launched with id: " + taskId);
	}
	
	/* Collect the result of a task run earlier.
	 */
	private static void commandTaskRetrieve(String arg0) throws Exception {
		long taskId = Long.valueOf(arg0);
		
		DurableExecutorService durableExecutorService
			= MyClient.hazelcastInstance.getDurableExecutorService("lab2");
				
		Future<String> future = durableExecutorService.retrieveResult(taskId);
		
		try {
			String result = future.get(1000, TimeUnit.MILLISECONDS);
			System.out.println(PREFIX + "Task result: '" + result + "'");
		} catch (ExecutionException executionException) {
			// Only a limited history of tasks is retained
			String message = executionException.getMessage();
			if (message.contains("StaleTaskIdException")) {
				System.out.println(PREFIX + "Task Id not found, did it ever exist?");
			} else {
				throw new RuntimeException(executionException);
			}
		} catch (TimeoutException timeoutException) {
			System.out.println(PREFIX + "Task is still running");
		}
	}

	/* Select the odd numbered Quotes, and run the entry processor
	 * on each to update on the servers.
	 */
	private static void commandTouchQuotes() throws Exception {
		String mapName = "lab2" + Quote.class.getSimpleName();
		IMap<Integer, Quote> quoteMap = MyClient.hazelcastInstance.getMap(mapName);

		Set<Integer> targetKeys = new TreeSet<>();

		for (Integer key : quoteMap.keySet()) {
			if (key % 2 != 0) {
				targetKeys.add(key);
			}
		}
		
		if (targetKeys.size()==0) {
			System.out.println(PREFIX + "No data to update");
			return;
		}

		long now = System.currentTimeMillis();
		MyQuoteTouchProcessor myQuoteTouchProcessor = new MyQuoteTouchProcessor(now);

		quoteMap.executeOnKeys(targetKeys, myQuoteTouchProcessor);
		System.out.println(PREFIX + "Updated " + targetKeys + " to " + now);
	}

}
