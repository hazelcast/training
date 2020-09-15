package com.hazelcast.platform.training.jet.jobs;

import static com.hazelcast.function.Functions.wholeItem;
import static com.hazelcast.jet.Traversers.traverseArray;
import static com.hazelcast.jet.aggregate.AggregateOperations.counting;
import static java.util.Comparator.comparingLong;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import com.hazelcast.collection.IQueue;

/*
 * Copyright (c) 2008-2020, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sinks;
import com.hazelcast.jet.pipeline.SourceBuilder;
import com.hazelcast.jet.pipeline.StreamSource;
import com.hazelcast.jet.pipeline.WindowDefinition;
import com.hazelcast.platform.training.common.Utils;

/**
 * Jet Exercise 3 - Word Count Streaming Job Demonstrates a simple streaming
 * Word Count job in the Pipeline API. Reads text messages off a Hazelcast
 * in-memory message queue within a window. The job counts the unique words and
 * writes the result to a distributed Map. The results map contents is also and
 * prints ever 5 seconds.
 */
public class WordCountStream {

	private static final String STREAM_COUNTS = "streamCounts";

	private JetInstance jet;

	private static Pipeline buildPipeline() {

		Pattern delimiter = Pattern.compile("\\W+");
		Pipeline p = Pipeline.create();

		// Initial a Hazelcast distributed IQueue as custom streaming source
		StreamSource<String> queueSource = SourceBuilder.stream("TrainingSourceQueue",
				context -> new QueueContext<>(Utils.remoteHazelcastInstance(Utils.clientConfigForExternalHazelcast())
						.<String>getQueue("TrainingSourceQueue")))
				.<String>fillBufferFn(QueueContext::fillBuffer).build();
		
		// Ingest lines of text from source in real-time
		p.readFrom(queueSource).withIngestionTimestamps().flatMap(e -> traverseArray(delimiter.split(e.toLowerCase())))
				.filter(word -> !word.isEmpty()).groupingKey(wholeItem()).window(WindowDefinition.sliding(1_000, 1_000))
				// .window(WindowDefinition.tumbling(15_000))
				.aggregate(counting())
				// .writeTo(Sinks.map(STREAM_COUNTS)); // Shows usage for a local jet cluster map vs. remote
				.writeTo(Sinks.remoteMap(STREAM_COUNTS, Utils.clientConfigForExternalHazelcast())); // remote Map
		return p;
	}

	public static void main(String[] args) throws Exception {
		new WordCountStream().go();
	}

	private void go() {
		try {
			
            //TODO: Get the jet instance and create a new job name 'WordCountStreaming'.  
			
			Thread printResultsThread = new Thread(new Runnable() {
				public void run() {
					while (true) {
						printResults();
						try {
							Thread.sleep(5000);
						} catch (Exception e) {
						}
					}
				}

			});
			printResultsThread.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void printResults() {
		final int limit = 100;
		Map<String, Long> streamCountResultsMap = Utils
				.remoteHazelcastInstance(Utils.clientConfigForExternalHazelcast()).getMap(STREAM_COUNTS);
		System.out.println("==== Printing results ");
		StringBuilder sb = new StringBuilder(String.format(" Top %d entries are:%n", limit));
		sb.append("/-------+---------\\\n");
		sb.append("| Count | Word    |\n");
		sb.append("|-------+---------|\n");
		streamCountResultsMap.entrySet().stream().sorted(comparingLong(Map.Entry<String, Long>::getValue).reversed())
				.limit(limit).forEach(e -> sb.append(String.format("|%6d | %-8s|%n", e.getValue(), e.getKey())));
		sb.append("\\-------+---------/\n");

		System.out.println(sb.toString());
	}

	// Hazelcast distributed IQueue as a custom streaming source
	static class QueueContext<T> {
		static final int MAX_ELEMENTS = 1024;
		List<T> tempCollection = new ArrayList<>();
		IQueue<T> queue;

		public QueueContext(IQueue<T> queue) {
			this.queue = queue;
		}

		void fillBuffer(SourceBuilder.SourceBuffer<T> buf) {
			queue.drainTo(tempCollection, MAX_ELEMENTS);
			tempCollection.forEach(buf::add);
			tempCollection.clear();
		}
	}

}