package com.hazelcast.techops.training.bootcamp;

import java.io.IOException;
import java.io.Serializable;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import lombok.Data;

/**
 * <P>A quote for a quantity of something at a price per unit.
 * </P>
 * <P>Open quotes haven't been converted into orders.
 * </P>
 */

@SuppressWarnings("serial")
@Data
public class Quote implements IdentifiedDataSerializable, Serializable {
	
	private String  item;
	private float	priceEach;
	private int		quantity;
	private boolean open;
	private long lastUpdatedTime;

	/* Read fields in the same order as written.
	 */
	public void readData(ObjectDataInput objectDataInput) throws IOException {
		this.item = objectDataInput.readUTF();
		this.lastUpdatedTime = objectDataInput.readLong();
		this.open = objectDataInput.readBoolean();
		this.priceEach = objectDataInput.readFloat();
		this.quantity = objectDataInput.readInt();
	}

	/* Write fields in some order, here we chose alphabetical
	 */
	public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
		objectDataOutput.writeUTF(this.item);
		objectDataOutput.writeLong(this.lastUpdatedTime);
		objectDataOutput.writeBoolean(this.open);
		objectDataOutput.writeFloat(this.priceEach);
		objectDataOutput.writeInt(this.quantity);
	}

	public int getFactoryId() {
		return Constants.MY_DATASERIALIZABLE_FACTORY_ID;
	}

	public int getId() {
		return Constants.MY_QUOTE_TYPE_ID;
	}

}
