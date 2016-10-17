# Bootcamp labs

4 labs showing some features

## Lab 1
Uses Hazelcast only with server JVMs, known as "_embedded_" mode. Your application contains the Hazelcast cluster.

Some options demonstrate how clusters join together.

Basic data structures are shown -- maps, queues, topics & atomics. These are distributed on the cluster so
as resilient as you configure them to be.

## Lab 2
Uses Hazelcast in client-server mode. The cluster is the server JVMs acting collectively. Clients don't host data.

This example is mainly about grid operations, running processing *distributed* on the grid. Rather than pull
the data back to the client to modify and safe, instead runnables, callables and entry processors run on
one or more servers at the request of a client.

## Lab 3
Client-server again, but now for Spring users.

Use Hazelcast from Spring, in the usual @Bean style.

Or, use Hazelcast as an implementation of JRS107, the Java Caching Standard. Although the implementation
is Hazelcast (obviously!), calls are to a caching provider so agnostic of the implementation provider.

## Lab 4
A client-server example using Spring Data for Hazelcast.

The server uses Spring Boot to create a Hazelcast for minimal code.

The client is Spring, a Spring @Repository is used to retrieve data, much the same as any other Spring @Repository.

## NOTE 1
Maven builds executable Jar files, so you need to run as far as the _package_ phase. So try "*mvn install*".

## NOTE 2
If something doesn't work quite as expected, that's when you learn the most.
