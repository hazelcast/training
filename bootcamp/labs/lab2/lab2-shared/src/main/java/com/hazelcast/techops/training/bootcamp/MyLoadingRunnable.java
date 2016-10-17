package com.hazelcast.techops.training.bootcamp;

import java.io.Serializable;
import java.time.LocalDate;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;

import lombok.extern.slf4j.Slf4j;

/**
 * <P>Load the test data.
 * </P>
 * <P>The class is {@link HazelcastInstanceAware} so will get a reference to
 * the current Hazelcast injected before the {@link #run()} method is invoked.
 * </P>
 */
@Slf4j
@SuppressWarnings("serial")
public class MyLoadingRunnable implements HazelcastInstanceAware, Runnable, Serializable {

	private transient HazelcastInstance hazelcastInstance;

	public void setHazelcastInstance(HazelcastInstance arg0) {
		this.hazelcastInstance = arg0;
	}

	public void run() {
		log.info("run()");
		
		IMap<Integer, Order> orderMap = this.hazelcastInstance.getMap(Constants.ORDER_MAP_NAME);
		this._loadOrders(orderMap);

		IMap<Integer, Quote> quoteMap = this.hazelcastInstance.getMap(Constants.QUOTE_MAP_NAME);
		this._loadQuotes(quoteMap);

		IMap<String, Stock> stockMap = this.hazelcastInstance.getMap(Constants.STOCK_MAP_NAME);
		this._loadStock(stockMap);
	}

	// One existing order from yesterday, see quote 5.
	private void _loadOrders(IMap<Integer, Order> orderMap) {
		
		Order order5 = new Order();
		
		LocalDate dispatched = LocalDate.now().minusDays(1L);
		order5.setDispatched(dispatched);
		order5.setQuoteId(5);
		
		orderMap.set(5, order5);
	}

	// Five quotes, the fifth already converted into an order
	private void _loadQuotes(IMap<Integer, Quote> quoteMap) {
		Quote quote1 = new Quote();
		Quote quote2 = new Quote();
		Quote quote3 = new Quote();
		Quote quote4 = new Quote();
		Quote quote5 = new Quote();
		
		quote1.setItem("apples");
		quote1.setPriceEach(1.1f);
		quote1.setQuantity(11);
		quote2.setItem("bananas");
		quote2.setPriceEach(2.2f);
		quote2.setQuantity(22);
		quote3.setItem("carrots");
		quote3.setPriceEach(3.3f);
		quote3.setQuantity(33);
		quote4.setItem("dates");
		quote4.setPriceEach(4.4f);
		quote4.setQuantity(44);
		quote5.setItem("elderberry");
		quote5.setPriceEach(5.5f);
		quote5.setQuantity(55);
		
		final long TODAY = System.currentTimeMillis();
		final long ONE_DAY = 24 * 60 * 60 * 1000L;
		
		quote1.setOpen(true);
		quote1.setLastUpdatedTime(TODAY - 1 * ONE_DAY);
		quote2.setOpen(true);
		quote2.setLastUpdatedTime(TODAY - 2 * ONE_DAY);
		quote3.setOpen(true);
		quote3.setLastUpdatedTime(TODAY - 3 * ONE_DAY);
		quote4.setOpen(true);
		quote4.setLastUpdatedTime(TODAY - 4 * ONE_DAY);
		quote5.setOpen(false);
		quote5.setLastUpdatedTime(TODAY - 5 * ONE_DAY);
		
		// Use set() not put() as we don't care to know the previous value (it was null!)
		quoteMap.set(1, quote1);
		quoteMap.set(2, quote2);
		quoteMap.set(3, quote3);
		quoteMap.set(4, quote4);
		quoteMap.set(5, quote5);
	}

	// Implausible stock quantities, arranged so there are not enough apples for quote 1
	private void _loadStock(IMap<String, Stock> stockMap) {
		
		Stock stock1 = new Stock();
		Stock stock2 = new Stock();
		Stock stock3 = new Stock();
		Stock stock4 = new Stock();
		Stock stock5 = new Stock();
		Stock stock6 = new Stock();
		Stock stock7 = new Stock();

		
		stock1.setItem("apples");
		stock1.setQuantity(10);
		stock2.setItem("bananas");
		stock2.setQuantity(10*10);
		stock3.setItem("carrots");
		stock3.setQuantity(10*10*10);
		stock4.setItem("dates");
		stock4.setQuantity(10*10*10*10);
		stock5.setItem("elderberry");
		stock5.setQuantity(10*10*10*10*10);
		stock6.setItem("figs");
		stock6.setQuantity(10*10*10*10*10*10);
		stock7.setItem("grapes");
		stock7.setQuantity(10*10*10*10*10*10*10);
		
		stockMap.set(stock1.getItem(), stock1);
		stockMap.set(stock2.getItem(), stock2);
		stockMap.set(stock3.getItem(), stock3);
		stockMap.set(stock4.getItem(), stock4);
		stockMap.set(stock5.getItem(), stock5);
		stockMap.set(stock6.getItem(), stock6);
		stockMap.set(stock7.getItem(), stock7);
	}

}
