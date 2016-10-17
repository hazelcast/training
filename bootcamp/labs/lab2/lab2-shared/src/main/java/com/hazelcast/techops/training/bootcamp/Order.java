package com.hazelcast.techops.training.bootcamp;

import java.io.IOException;
import java.time.LocalDate;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <P>An order is a quote that has been dispatched.
 * </P>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order implements IdentifiedDataSerializable {

	private LocalDate dispatched;
	private int quoteId;
	
	public void readData(ObjectDataInput objectDataInput) throws IOException {
		this.dispatched = objectDataInput.readObject();
		this.quoteId = objectDataInput.readInt();
	}

	public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
		objectDataOutput.writeObject(this.dispatched);
		objectDataOutput.writeInt(this.quoteId);
	}

	public int getFactoryId() {
		return Constants.MY_DATASERIALIZABLE_FACTORY_ID;
	}

	public int getId() {
		return Constants.MY_ORDER_TYPE_ID;
	}

}
