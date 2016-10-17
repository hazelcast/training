package com.hazelcast.techops.training.bootcamp;

import java.util.Map.Entry;

import com.hazelcast.map.EntryBackupProcessor;
import com.hazelcast.map.EntryProcessor;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <P>Update a quote, here only setting the last update timestamp
 * to the given argument. Essentially this is delta processing,
 * we send the value we want changed and apply it directly.
 * </P>
 * <P>Provide a method the master and any backup, so we don't
 * have to copy the master over to the backup, as this would
 * defeat the point of delta processing.
 * </P>
 */
@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class MyQuoteTouchProcessor implements EntryProcessor<Integer, Quote>, EntryBackupProcessor<Integer, Quote> {

	private long timestamp;
	
	public EntryBackupProcessor<Integer, Quote> getBackupProcessor() {
		return this;
	}

	public Object process(Entry<Integer, Quote> entry) {
		Quote quote = entry.getValue();
		quote.setLastUpdatedTime(timestamp);
		entry.setValue(quote);
		log.info("Updated '{}'", entry);
		return entry;
	}
	
	public void processBackup(Entry<Integer, Quote> entry) {
		Quote quote = entry.getValue();
		if (quote!=null) {
			quote.setLastUpdatedTime(timestamp);
			entry.setValue(quote);
			log.info("Updated backup '{}'", entry);
		}
	}

}
