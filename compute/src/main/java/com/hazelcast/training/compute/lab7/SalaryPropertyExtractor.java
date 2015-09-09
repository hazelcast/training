package com.hazelcast.training.compute.lab7;

import com.hazelcast.mapreduce.aggregation.PropertyExtractor;

public class SalaryPropertyExtractor implements PropertyExtractor<Employee, Integer> {

    @Override
    public Integer extract(Employee value) {
        return value.getSalaryPerMonth();
    }
}
