package com.hazelcast.techops.training.bootcamp;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hazelcast.core.IMap;

import lombok.extern.slf4j.Slf4j;

/**
 * <P>A Spring main class that loads spring beans from the {@code applicationContext.xml}
 * file. Annotations would do just as well, and would be the more modern approach.
 * </P>
 * <P>The XML file will create a Hazelcast client and use this to create a cache manager.
 * </P>
 * <HR/>
 * <P>Call the Fibonacci calculator. Depending on if this is the first
 * or later time this client is run, the results may be returned from the server or
 * calculated and stored.
 * </P>
 * <P>Note there are two variations on the calculation, imaginatively named
 * {@link Fibonacci#fib} and {@link Fibonacci#fib2}. Results will be the same, but
 * performance will not be.
 * </P>
 */
@Slf4j
public class SpringClientHazelcast {

	public static void main(String[] args) {
		System.setProperty("hazelcast.logging.type","slf4j");
		
		try(AbstractApplicationContext abstractApplicationContext
			= new ClassPathXmlApplicationContext("applicationContext.xml");) {
			
			Fibonacci fibonacci = abstractApplicationContext.getBean(Fibonacci.class);
			
			int result;

			for (Integer i : new Integer[] { 5, 4, 5}) {
				log.info("------------------------------------------------------------");
				log.info("Call fibonacci.fib({})", i);
				// result = fibonacci.fib(i);
				result = fibonacci.fib2(i, fibonacci);
				log.info("Called fibonacci.fib({}), result=={}", i, result);
			}


			IMap<?, ?> fib = (IMap<?, ?>) abstractApplicationContext.getBean("fibMapBean");
			log.info("------------------------------------------------------------");
			log.info("Map '{}'", fib.getName());
			if (fib.size()==0) {
				log.info("  Empty");
			} else {
				fib.entrySet().forEach(entry -> {
					log.info("  Key '{}', Value '{}'", entry.getKey(), entry.getValue());
				});
			}
			log.info("------------------------------------------------------------");

		}
	}

}
