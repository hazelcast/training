package com.hazelcast.techops.training.bootcamp;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.listener.EntryUpdatedListener;
import com.hazelcast.map.listener.EntryAddedListener;

import lombok.extern.slf4j.Slf4j;

/**
 * <P>Can listen for specific types of events, more efficient
 * than to listen for everything.
 * </P>
 * <P>Chose at least one or all from:
 * <OL>
 * <LI>Added</LI>
 * <LI>Evicted</LI>
 * <LI>Expired</LI>
 * <LI>Merged in from another cluster</LI>
 * <LI>Removed</LI>
 * <LI>Updated</LI>
 * </OL>
 * </P>
 */
@SuppressWarnings("rawtypes")
@Slf4j
public class MyLoggingListener 
	implements EntryAddedListener, EntryUpdatedListener {

	public void entryUpdated(EntryEvent entryEvent) {
		this._log(entryEvent);
	}

	public void entryAdded(EntryEvent entryEvent) {
		this._log(entryEvent);
	}

	private void _log(EntryEvent entryEvent) {
		log.info("Event '{}' in map '{}', key=='{}', new value=='{}'",
				entryEvent.getEventType(), entryEvent.getSource(),
				entryEvent.getKey(), entryEvent.getValue());
	}

}
