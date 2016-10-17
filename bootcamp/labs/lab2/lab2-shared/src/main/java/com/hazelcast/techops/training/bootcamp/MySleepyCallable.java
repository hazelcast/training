package com.hazelcast.techops.training.bootcamp;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.Callable;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <P>A fairly typical callable.
 * </P>
 * <P>How this differs from normal is this class has an
 * ability to be serialized. So a Hazelcast client can
 * create it and send it to the server(s) to run.
 * </P>
 */
@NoArgsConstructor
@Slf4j
@SuppressWarnings("serial")
public class MySleepyCallable implements Callable<String>, Serializable {
	private static final long STEP = 5000;
	
	private long duration;
	
	public MySleepyCallable(long arg0) {
		this.duration = Math.abs(arg0);
	}

	/* Count down and return a string.
	 */
	public String call() throws Exception {
		log.info("Start run of {} for duration {}ms", this, this.duration);
		
		while (this.duration > STEP) {
			log.info("During run of {} for duration {}ms", this, this.duration);
			this.duration -= STEP;
			Thread.sleep(STEP);
		}

		Thread.sleep(this.duration);
		
		log.info("End run of {}", this);
		return "Ended at " + new Date();
	}

}
