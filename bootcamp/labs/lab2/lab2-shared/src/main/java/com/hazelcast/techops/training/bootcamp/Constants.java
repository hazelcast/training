package com.hazelcast.techops.training.bootcamp;

public class Constants {

	public static final int MY_DATASERIALIZABLE_FACTORY_ID  = 1;

	public static final int MY_ORDER_TYPE_ID = 1000;
	public static final int MY_QUOTE_TYPE_ID = 1001;
	public static final int MY_STOCK_TYPE_ID = 1002;

	public static final String ORDER_MAP_NAME = "lab2" + Order.class.getSimpleName();
	public static final String QUOTE_MAP_NAME = "lab2" + Quote.class.getSimpleName();
	public static final String STOCK_MAP_NAME = "lab2" + Stock.class.getSimpleName();
}
