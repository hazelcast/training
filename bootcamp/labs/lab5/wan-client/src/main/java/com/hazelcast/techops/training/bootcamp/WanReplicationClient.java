package com.hazelcast.techops.training.bootcamp;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.techops.training.bootcamp.util.LicenseUtil;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * <h1> Client instance of WAN Replication test </h1>
 *
 * <p> Connect to one of the two clusters and perform distributed Map operations (read/write).
 * To verify data updates from other cluster through WAN Replication, launch another instance
 * of this app and connect to the other cluster. Once connected, perform a read operation on
 * the key that was stored in other cluster.
 * </p>
 *
 * <p> Address of both clusters are hard coded.</p>
 *
 */
public class WanReplicationClient {

    private static IMap MAP;
    private static HazelcastInstance HAZELCAST;

    public static void main(String[] args) {
        System.out.println("Write \"Help\" for the list of available commands:");
        Scanner reader = new Scanner(System.in);
        while(true) {
            sleepMillis(100);
            System.out.print("Command: ");
            String command = reader.nextLine().toLowerCase();

            if(command.equalsIgnoreCase("help")) {
                printAvailableCommands();
            }

            if (command.equalsIgnoreCase("connect_cluster_a")) {
                if (isClientConnected()) {
                    System.out.println("Wrong command, Client is already connected.");
                    printAvailableCommands();
                    continue;
                }
                connectClusterAandInitialize();
            }

            if (command.equalsIgnoreCase("connect_cluster_b")) {
                if (isClientConnected()) {
                    System.out.println("Wrong command, Client is already connected.");
                    printAvailableCommands();
                    continue;
                }
                connectClusterBandInitialize();
            }

            if(command.startsWith("get")) {
                String key = command.split(" ")[1];
                doReadOnKey(key);
            }

            if(command.startsWith("put")) {
                String key = command.split(" ")[1];
                String value = command.split(" ")[2];
                doDistributedWrite(key, value);
            }

            if(command.equalsIgnoreCase("shutdown")) {
                shutdown();
            }
        }
    }

    private static void shutdown() {
        if(null != HAZELCAST)
            HAZELCAST.shutdown();
        System.exit(0);
    }

    private static void doDistributedWrite(String key, String value) {
        System.out.println("Writing key and value to cluster map");
        System.out.println("Old value: "+MAP.put(key, value));
    }

    private static void doReadOnKey(String key) {
        System.out.println("Reading value for key: "+key+"  Value: "+MAP.get(key));
    }

    private static void connectClusterBandInitialize() {
        ClientConfig config = new ClientConfig();
        config.getNetworkConfig().addAddress("127.0.0.1:5801");
        config.getGroupConfig().setName("ClusterB-Name");
        config.getGroupConfig().setPassword("ClusterB-Pass");

        config.setLicenseKey(LicenseUtil.KEY);

        HAZELCAST = HazelcastClient.newHazelcastClient(config);
        MAP = HAZELCAST.getMap("WAN_MAP");
    }

    private static void connectClusterAandInitialize() {
        ClientConfig config = new ClientConfig();
        config.getNetworkConfig().addAddress("127.0.0.1:5701");
        config.getGroupConfig().setName("ClusterA-Name");
        config.getGroupConfig().setPassword("ClusterA-Pass");

        config.setLicenseKey(LicenseUtil.KEY);

        HAZELCAST = HazelcastClient.newHazelcastClient(config);
        MAP = HAZELCAST.getMap("WAN_MAP");
    }

    private static void printAvailableCommands() {
        System.out.println("Available Commands: ");
        System.out.println("1) CONNECT_CLUSTER_A\n"
                + "2) CONNECT_CLUSTER_B\n"
                + "3) PUT [key] [value]\n"
                + "4) GET [key]\n"
                + "5) Shutdown");
    }

    private static boolean isClientConnected() {
        return HAZELCAST != null && HAZELCAST.getCluster() != null;
    }

    private static void sleepMillis(long duration) {
        try {
            TimeUnit.MILLISECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
