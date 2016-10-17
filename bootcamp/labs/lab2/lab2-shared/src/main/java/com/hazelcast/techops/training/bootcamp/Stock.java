package com.hazelcast.techops.training.bootcamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <P>A domain model that doesn't reference Hazelcast.
 * </P>
 * <P>In a <I>key-value</I> store you don't need to embed
 * the key in the value, but it can be useful when working
 * with other stores.
 * </P>
 * <P>Serialization is handled by {@link StockSerializer}
 * </P>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
	
	// Key
	private String item;
	private int    quantity;

}
