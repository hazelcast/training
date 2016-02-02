package com.hazelcast.training.caching.lab4;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.EntryObject;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import com.hazelcast.training.caching.dto.Company;

import java.util.HashSet;
import java.util.Set;

import static com.hazelcast.query.Predicates.*;
import static com.hazelcast.training.caching.common.LabConstants.IMAP_NAME;

public class CompanyPredicates {

    static HazelcastInstance hz = Hazelcast.newHazelcastInstance();
    static IMap<String, Company> companyMap = hz.getMap(IMAP_NAME);

    public static void main(String[] args) {

        companyMap.put("1", new Company("Hazelcast", true, 36));
        companyMap.put("2", new Company("Oracle", true, 50));
        companyMap.put("3", new Company("Tangosol", false, 20));
        companyMap.put("4", new Company("Nike", true, 35));
        companyMap.put("5", new Company("Red Hat", true, 60));
        companyMap.put("6", new Company("Microsoft", false, 43));

        System.out.println("Get with name Hazelcast");
        for (Company person : getWithCompanyName("Hazelcast")) {
            System.out.println(person);
        }

        System.out.println("Get not with name Hazelcast");
        for (Company person : getNotWithNoCompanyName("Hazelcast")) {
            System.out.println(person);
        }

        System.out.println("Find company Nike and associates number 35");
        for (Company person : getWithCompanyNameAndAssociatesNum("Nike", 35)) {
            System.out.println(person);
        }

        System.out.println("Find company Hazelcast with 37 associates");
        for (Company person : getWithCompanyNameAndAssociatesNum("Hazelcast", 37)) {
            System.out.println(person);
        }
    }

    public static Set<Company> getWithCompanyNameNaive(String name) {
        Set<Company> result = new HashSet<Company>();
        for (Company person : companyMap.values()) {
            if (person.getCompanyName().equals(name)) {
                result.add(person);
            }
        }

        return result;
    }

    public static Set<Company> getNotWithNoCompanyName(String companyName) {
        Predicate namePredicate = equal("companyName", companyName);
        Predicate predicate = not(namePredicate);
        return (Set<Company>) companyMap.values(predicate);
    }

    public static Set<Company> getWithCompanyName(String companyName) {
        Predicate predicate = com.hazelcast.query.Predicates.equal("companyName", companyName);
        return (Set<Company>) companyMap.values(predicate);
    }

    public Set<Company> getWithNameAndAgeSimplified(String companyName, int num) {
        EntryObject e = new PredicateBuilder().getEntryObject();
        Predicate predicate =
            e.get("companyName").equal(companyName).and(e.get("associateNum").equal(num));
        return (Set<Company>) companyMap.values(predicate);
    }

    public static Set<Company> getWithCompanyNameAndAssociatesNum(String companyName, int num) {
        Predicate namePredicate = equal("companyName", companyName);
        Predicate agePredicate = equal("associateNum", num);
        Predicate predicate = and(namePredicate, agePredicate);
        return (Set<Company>) companyMap.values(predicate);
    }

    public Set<Company> getWithCompanyNameOrAssociatesNum(String companyName, int num) {
        Predicate namePredicate = equal("companyName", companyName);
        Predicate agePredicate = equal("associateNum", num);
        Predicate predicate = or(namePredicate, agePredicate);
        return (Set<Company>) companyMap.values(predicate);
    }
}
