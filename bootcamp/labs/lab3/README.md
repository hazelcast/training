# Bootcamp lab3

## Objective
Demonstrate use of Spring with Hazelcast directly or as a JCache implementation provider.
Although it's an older approach, show here is some XML rather than pure annotations.

## Step 1
Start the cluster, run this once *java -jar target/bootcamp-lab3-server.jar*.

Although it's a single Jar, it actually starts three Hazelcast server instances in
the same JVM.

Config here is done with Java. In Lab 4 we are lazy/efficient, and let a Hazelcast @Bean
be created for us. Here though we need three Hazelcast server @Beans, one for each.

## Step 2
Run the Spring caching client, using *java -jar target/bootcamp-lab3-client-hazelcast.jar*.

This uses one of two implementations for the Fibonacci series. Try swapping it from one
to the other to see what happens.

The main thing to note here is if you run this twice, the cached calculation results are
still on the servers (unless you restart), so the second client run never needs to do any
calculations.

## Step 3
Run the JCache caching client, using *java -jar target/bootcamp-lab3-client-jcache.jar*.

The idea is largely the same as for Step 2, except using JSR107's JCache standard
implementation.

The processing here is multiplication. If you examine the cached value you'll see that
the key is a compound of the method arguments.

