package com.hazelcast.techops.training.bootcamp;

import java.util.List;

import org.springframework.data.hazelcast.repository.HazelcastRepository;

public interface CapitalRepository extends HazelcastRepository<Capital, String> {

	public List<Capital> 	findByPopulationGreaterThan(int i);
	
}
