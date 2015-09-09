package com.hazelcast.training.compute.lab7;

import com.hazelcast.mapreduce.aggregation.PropertyExtractor;

public class FirstNamePropertyExtractor implements PropertyExtractor<Employee, String> {

    @Override
    public String extract(Employee value) {
        return value.getFirstName();
    }
}
