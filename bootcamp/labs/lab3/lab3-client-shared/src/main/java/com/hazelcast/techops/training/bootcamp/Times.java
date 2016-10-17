package com.hazelcast.techops.training.bootcamp;

import java.util.concurrent.atomic.AtomicLong;

import javax.cache.annotation.CacheResult;

import lombok.extern.slf4j.Slf4j;

/**
 * <P>Another example of method caching, the Times table.
 * </P>
 * <P>There are two versions here.
 * </P>
 * <OL>
 * <LI><B>{@code goodTimes}</B> A good implementation,
 * the result is the product of the arguments, so fine
 * for caching.
 * </LI>
 * <LI><B>{@code badTimes}</B> A bad implementation, as
 * this method has side effects (it updates a static variable).
 * Consequently if we using method caching, the method doesn't
 * always run which is what you'd want, but the counter deosn't
 * update which isn't what you want.
 * </LI>
 * </OL>
 *  * 
 * <P><B>NOTE:</B> This uses the {@code @CacheResult} annotation,
 * so is for JCache not Spring caching.
 * </P>
 *
 * TODO: Is it possible to annotate methods with {@code @Cacheable}
 * and {@code @CacheResult}.
 */
@Slf4j
public class Times {
	
	/**
	 * <P>Hazelcast has {@link com.hazelcast.core.IAtomicLong}.
	 * Try changing to use this instead...
	 * <?p>
	 */
	private static AtomicLong usageCounter = new AtomicLong();

    // A method suitable for caching
	
	@CacheResult(cacheName="goodCache")
	public int goodTimes(int i, int j) {
		log.info("goodTimes({},{})", i, j);
		return i * j;
	}

    // A method unsuitable for caching, but do so anyway
	
	@CacheResult(cacheName="badCache")
	public int badTimes(int i, int j) {
		log.info("badTimes({},{})", i, j);
		usageCounter.incrementAndGet();
		return i * j;
	}
	
	public static long getUsageCounter() {
		return usageCounter.get();
	}
}
