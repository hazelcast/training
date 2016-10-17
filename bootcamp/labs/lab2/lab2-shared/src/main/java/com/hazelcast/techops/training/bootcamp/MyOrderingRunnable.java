package com.hazelcast.techops.training.bootcamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;

import lombok.extern.slf4j.Slf4j;

/**
 * <P>"Business logic" -- change any open quote to closed and create an order,
 * if sufficient stock is available.
 * </P>
 * <P>Notes
 * <OL>
 * <LI>The runnable uses the <U>local</U> quotes, the subset held on this
 * server JVM when executing. Each server processes their own share in
 * parallel.
 * </LI>
 * <LI>Take an optimistic approach to concurrency, rather than locking or
 * transactions. Stock updates use the {@link com.hazelcast.core.IMap#replace()}
 * call to update quantities if nothing else has happened concurrently.
 * </LI>
 * </OL>
 * </P>
 * <P>The class is {@link HazelcastInstanceAware} so will get a reference to
 * the current Hazelcast injected before the {@link #run()} method is invoked.
 * </P>
 */
@Slf4j
@SuppressWarnings("serial")
public class MyOrderingRunnable implements HazelcastInstanceAware, Runnable, Serializable {

	private transient HazelcastInstance hazelcastInstance;
	private transient IMap<Integer, Order> orderMap;
	private transient IMap<Integer, Quote> quoteMap;
	private transient IMap<String, Stock> stockMap;

	public void setHazelcastInstance(HazelcastInstance arg0) {
		this.hazelcastInstance = arg0;
		this.orderMap = this.hazelcastInstance.getMap(Constants.ORDER_MAP_NAME);
		this.quoteMap = this.hazelcastInstance.getMap(Constants.QUOTE_MAP_NAME);
		this.stockMap = this.hazelcastInstance.getMap(Constants.STOCK_MAP_NAME);
	}

	public void run() {
		log.info("START === run() === START");
		
		Set<Integer> quoteKeys = this.quoteMap.localKeySet();
		log.info("Considering {}", quoteKeys);

		for (Integer quoteKey : quoteKeys) {
			Quote quote = this.quoteMap.get(quoteKey);
			Stock oldStock = this.stockMap.get(quote.getItem());
			
			if (!quote.isOpen()) {
				log.info("Ignoring quote {}, closed", quoteKey);
			} else {
				if (orderMap.containsKey(quoteKey)) {
					log.info("Ignoring quote {}, order exists", quoteKey);
				} else {
					if (oldStock.getQuantity() < quote.getQuantity()) {
						log.info("Not enough stock, have {} need {} for {}", 
								oldStock.getQuantity(), quote.getQuantity(), quoteKey);
					} else {
						Stock newStock = new Stock(oldStock.getItem(), oldStock.getQuantity() - quote.getQuantity());
						
						// Try to change stock levels
						boolean worked = this.stockMap.replace(newStock.getItem(), oldStock, newStock);
						
						if (!worked) {
							log.info("No longer enough stock for {}", quoteKey);
						} else {
							Order order = new Order(LocalDate.now(), quote.getId());
							log.info("Adding order {}", order);
							this.orderMap.set(order.getId(), order);
							
							quote.setOpen(false);
							log.info("Closing quote {}", quoteKey);
							this.quoteMap.set(quoteKey, quote);
						}
					}
				}
			}
		}
		
		log.info("END === run() === END");
	}

}
