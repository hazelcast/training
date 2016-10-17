package com.hazelcast.techops.training.bootcamp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CapitalService {

	@Autowired
	public CapitalRepository capitalRepository;
	
	public List<Capital> tenMillionPlus() {
		return this.capitalRepository.findByPopulationGreaterThan(10_000_000);
	}
	
	public Iterable<Capital> namesDescending() {
		Sort sort = new Sort(Sort.Direction.DESC, "name");
		return this.capitalRepository.findAll(sort);
	}
}
