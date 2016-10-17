package com.hazelcast.techops.training.bootcamp;

import java.util.Iterator;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;

import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import lombok.extern.slf4j.Slf4j;


/**
 * <P>A JSR107 style caching example, using Spring beans but not Spring caching.
 * <HR/>
 * <P>Processing here is called to two methods {@link Times#goodTimes} and 
 * {@link Times#badTimes}.
 * </P>
 * <P> Both are correct, but the {@link Times#badTimes} is bad as it has a
 * side effect. When it runs it updates a static variable. So, if caching
 * is involved and it doesn't run, the static variable is not updated and
 * the count is wrong.
 * </P>
 */
@Slf4j
public class SpringClientJCache {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		System.setProperty("hazelcast.logging.type","slf4j");

		try(AbstractApplicationContext abstractApplicationContext
				= new ClassPathXmlApplicationContext("applicationContext.xml");) {

			JCacheCacheManager jCacheCacheManager = 
					(JCacheCacheManager) abstractApplicationContext.getBean("cacheManager");
									
			CacheManager cacheManager = jCacheCacheManager.getCacheManager();
			
			// Create the caches that we will need
			MutableConfiguration mutableConfiguration = new MutableConfiguration();
			cacheManager.createCache("goodCache", mutableConfiguration);
			cacheManager.createCache("badCache", mutableConfiguration);

			int result;

			Times times = abstractApplicationContext.getBean(Times.class);

			for (String ordinal : new String[] { "First",  "Second"}) {
				log.info("------------------------------------------------------------");
				log.info(ordinal + " call times.goodTimes(2,3)");
				result = times.goodTimes(2,3);
				log.info(ordinal + " call times.goodTimes(2,3), result=={}", result);
				
				log.info("------------------------------------------------------------");
				log.info(ordinal + " call times.badTimes(2,3)");
				result = times.badTimes(2,3);
				log.info(ordinal + " call times.badTimes(2,3), result=={}", result);
			}

			// Usage counter for Times.badTimes() is probably wrong due to method caching.
			log.info("------------------------------------------------------------");
			log.info("Times.getUsageCounter();, result=={}", Times.getUsageCounter());

			cacheManager.getCacheNames().forEach(cacheName -> {
				log.info("------------------------------------------------------------");
				log.info("Cache '{}'", cacheName);
				Cache cache = cacheManager.getCache(cacheName);
				Iterator<Cache.Entry> iterator = cache.iterator();
				int count=0;
				while (iterator.hasNext()) {
					Cache.Entry entry = iterator.next();
					log.info("  Key '{}', Value '{}'", entry.getKey(), entry.getValue());
					count++;
				}
				if (count==0) {
					log.info("  Empty");
				}
			});
			log.info("------------------------------------------------------------");
			
	        cacheManager.close();
		}
		
		System.exit(0);
	}

}
