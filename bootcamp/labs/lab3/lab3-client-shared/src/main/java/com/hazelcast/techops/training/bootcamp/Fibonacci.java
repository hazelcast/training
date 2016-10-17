package com.hazelcast.techops.training.bootcamp;

import org.springframework.cache.annotation.Cacheable;

import lombok.extern.slf4j.Slf4j;

/**
 * <P>Use the Fibonacci series as a demonstration of the power of
 * method caching.
 * </P>
 * <P>The Fibonacci series goes 1, 1, 2, 3, 5, 8... etc, where 
 * each result is the sum of the previous two. 
 * </P>
 * <P>Ordinarily this is expontential processing time, each call
 * to the Fibonacci function for large numbers results in multiple (two!)
 * calls to the Fibonacci function for smaller numbers. Where
 * caching is involved, the processing times changes from 
 * exponential to linear, a massive saving.
 * </P>
 * <P>The <I>cacheNames</I> field indicates where the result will
 * be stored.
 * </P>
 * 
 * <P><B>NOTE:</B> This uses the {@code @Cacheable} annotation,
 * so is for Spring caching not JCache.
 * </P>
 * 
 * TODO: Is it possible to annotate methods with {@code @Cacheable}
 * and {@code @CacheResult}.
 */
@Slf4j
public class Fibonacci {
	
	/**
	 * <P>The simple implementation of the Fibonacci calculation.
	 * </P>
	 * <P>This would appear to call itself recursively, and each
	 * of these calls could benefit from cache lookup.
	 * </P>
	 * <P>Most likely the JVM will optimise the call so only the
	 * first call is a method call (and so caches a result).
	 * </P>
	 */
	@Cacheable(cacheNames="fibMap")
	public int fib(int i) {
		log.info("fib({})", i);
		if (i<3) {
			return 1;
		} else {
			return this.fib(i-1) + this.fib(i-2);
		}
	}

	/**
	 * <P>An optimisation-unfriendly version of the Fibonacci
	 * calculation. Take in an object (which should be {@code this})
	 * to use for the method calls, forcing these to be proper
	 * calls and so allowing the caching of results.
	 * </P>
	 * <P>As we don't want the Fibonacci object to be part of
	 * the cache key, indicate what is the cache key.
	 * </P>
	 */
	@Cacheable(cacheNames="fibMap", key="#i")
	public int fib2(int i, Fibonacci that) {
		log.info("fib2({})", i);
		if (i<3) {
			return 1;
		} else {
			return that.fib2(i-1, that) + that.fib2(i-2, that);
		}
	}

}
