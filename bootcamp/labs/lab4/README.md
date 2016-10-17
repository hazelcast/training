# Bootcamp lab4

## Objective
Use Spring-Data-Hazelcast for resilient data access in a familiar Spring like way,
no knowledge of Hazelcast is really needed.

## Step 1 - cluster formation
Start a couple of instances of the server. The first to start will load some test data.

## Step 2 - Spring client
Run the Spring client.

All Hazelcast plumbing is encapsulated in `HazelcastSpringConfiguration`. Everything
else is normal Spring, use of @Service and @Repository.

Note in the @Repository how methods are defined and Spring deduces the implementation.
Add some more if you like.

## Step 3 - resilience
Kill a server and re-run a client. A Spring developer gets resilience!
