package com.hazelcast.techops.training.bootcamp;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import lombok.extern.slf4j.Slf4j;

/**
 * <P>Load test data once.
 * </P>
 */
@Component
@Slf4j
public class TestDataLoader implements CommandLineRunner {
	
	@Autowired
	private HazelcastInstance hazelcastInstance;

	@Override
	public void run(String... arg0) throws Exception {
		IMap<String, Capital> capitalMap = this.hazelcastInstance.getMap("capital");
		
		if (capitalMap.size()==0) {
			Arrays.stream(capitals).forEach((Object[] data) -> {
				Capital capital = new Capital();
				
				capital.setName(data[1].toString());
				capital.setCountry(data[0].toString());
				capital.setPopulation((Integer)data[2]);

				capitalMap.put(capital.getName(), capital);
			});
			
			log.info("Loaded {} to map '{}'", capitals.length, capitalMap.getName());
		}
	}

	// https://fr.wikipedia.org/wiki/Liste_des_capitales_du_monde_par_population
	
	private static final Object[][] capitals = new Object[][] {
		{ "China", 			"Peking", 		20_693_000 },
		{ "Japan", 			"Tokyo",  		13_189_000 },
		{ "Russia", 		"Moscow", 		11_541_000 },
		{ "South Korea",	"Seoul", 		10_528_000 },
		{ "Indonesia", 		"Jakarta", 		10_187_000 },
		{ "Mexico", 		"Mexico City", 	8_851_000 },
		{ "Great Britain", 	"London", 		8_630_000 },
		{ "Peru", 			"Lima", 		8_481_000 },
		{ "Thailand", 		"Bangkok", 		8_249_000 },
		{ "Iran", 			"Tehran", 		8_154_000 },
		{ "Columbia", 		"Bogota", 		7_613_000 },
		{ "Egypt", 			"Cairo", 		7_438_000 },
		{ "Iran",	 		"Baghdad", 		7_216_000 },
		{ "Hong Kong", 		"Hong Kong", 	7_136_000 },
		{ "Bangladesh", 	"Dhaka", 		8_906_000 },
		{ "Singapore", 		"Singapore", 	5_312_000 },
		{ "Turkey", 		"Ankara", 		5_150_000 },
		{ "Chile", 			"Santiago", 	5_084_000 },
		{ "Saudi Arabia", 	"Riyadh", 		4_878_000 },
		{ "Democratic Republic of the Congo",
							"Kinshasha",	4_385_000 },
	};
		
}
