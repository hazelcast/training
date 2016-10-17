package com.hazelcast.techops.training.bootcamp;

import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyDataSerializableFactory implements DataSerializableFactory {

	/* Receive the code number for the kind of class to create.
	 */
	public IdentifiedDataSerializable create(int typeId) {
		
		if (typeId==Constants.MY_ORDER_TYPE_ID) {
			return new Order();
		}
		if (typeId==Constants.MY_QUOTE_TYPE_ID) {
			return new Quote();
		}
		
		log.error("create({}) ==> null", typeId);
		return null;
	}

}
