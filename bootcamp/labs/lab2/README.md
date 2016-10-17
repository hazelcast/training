# Bootcamp lab2

## Objective
Explore processing across the servers

## Step 1
Start three servers using *java -jar target/bootcamp-lab2-server.jar* and one client with *java -jar target/bootcamp-lab2-client.jar*

## Step 2
Invoke a runnable, using __LOAD_TESTDATA__. 

Then use the __LIST__ command to inspect the test data. Or try __SQL__ to see a subset of the quotes matching a query.

The test data here is an online fruit retailer. Open quotes represent
a request for stock. If stock is available, a quote can be closed and an order dispatch created.

## Step 3
Invoke __TOUCH_QUOTES__ to amend an individual field on some quotes.

Again, try __SQL__ to see the change.

## Step 4
Run __CLOSE_QUOTES__ to search for open quotes that can be closed, in parallel across
the running servers.

## Step 5
Run __TASK_LAUNCH__ with a paramter of 30000 (30,000 milliseconds==30 seconds) to submit
processing that runs for some time. Note the returned task number.

If you like, find which server is running the task and kill it. The task gets restarted on
another server.

Finally, use __TASK_RETRIEVE__ with the task number to retrieve the result.
