package com.hazelcast.techops.training.bootcamp;

import java.io.IOException;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;

/**
 * <P>Serialization for a domain model that doesn't want to
 * embed any Hazelcast serialization method.
 * </P>
 */
public class StockSerializer implements StreamSerializer<Stock> {

	public void destroy() {
	}

	public int getTypeId() {
		return Constants.MY_STOCK_TYPE_ID;
	}

	/* Read all the fields in the sequence that the write() method
	 * below sends them.
	 */
	public Stock read(ObjectDataInput objectDataInput) throws IOException {
		Stock stock = new Stock();
		
		stock.setItem(objectDataInput.readUTF());
		stock.setQuantity(objectDataInput.readInt());

		return stock;
	}

	/* Write all fields in the some order, here we chose alphabetical.
	 */
	public void write(ObjectDataOutput objectDataOutput, Stock stock) throws IOException {
		objectDataOutput.writeUTF(stock.getItem());
		objectDataOutput.writeInt(stock.getQuantity());
	}

}
