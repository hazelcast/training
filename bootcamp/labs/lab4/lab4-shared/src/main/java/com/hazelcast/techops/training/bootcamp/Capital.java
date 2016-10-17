package com.hazelcast.techops.training.bootcamp;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;

import lombok.Data;

@Data
@KeySpace("capital")
public class Capital implements Comparable<Capital>, Serializable {
    private static final long serialVersionUID = 1L;

	@Id
	private String name;
	private String country;
	private int	   population;
	
	public int compareTo(Capital that) {
		return this.name.compareTo(that.getName());
	}

}
