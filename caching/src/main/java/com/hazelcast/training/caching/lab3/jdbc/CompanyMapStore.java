package com.hazelcast.training.caching.lab3.jdbc;

import com.google.common.collect.Sets;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MapLoaderLifecycleSupport;
import com.hazelcast.core.MapStore;
import com.hazelcast.training.caching.dto.Company;

import java.sql.*;
import java.util.*;

import static java.lang.String.format;

public class CompanyMapStore implements MapStore<Integer, Company>, MapLoaderLifecycleSupport {

    private Connection con;
    private PreparedStatement allKeysStatement;

    public CompanyMapStore() {
    }

    public synchronized void delete(Integer key) {
        System.out.println("Delete:" + key);
        try {
            con.createStatement().executeUpdate(
                format("delete from company where id = %s", key));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void store(Integer key, Company value) {
        try {
            con.createStatement().executeUpdate(
                format("insert into company values(%s,'%s')", key, value.getCompanyName()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void storeAll(Map<Integer, Company> map) {
        for (Map.Entry<Integer, Company> entry : map.entrySet())
            store(entry.getKey(), entry.getValue());
    }

    public synchronized void deleteAll(Collection<Integer> keys) {
        for (Integer key : keys)
            delete(key);
    }

    public synchronized Company load(Integer key) {
        try {
            ResultSet resultSet = con.createStatement().executeQuery(
                format("select name from company where id =%s", key));
            try {
                if (!resultSet.next())
                    return null;
                String name = resultSet.getString(1);
                return new Company(key, name);
            } finally {
                resultSet.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized Map<Integer, Company> loadAll(Collection<Integer> keys) {
        Map<Integer, Company> result = new HashMap<Integer, Company>();
        for (Integer key : keys)
            result.put(key, load(key));
        return result;
    }

    public Set<Integer> loadAllKeys() {
        StatementIterable<Integer> keys = new StatementIterable<Integer>(allKeysStatement);
        return Sets.newHashSet(keys);
    }

    @Override
    public void init(HazelcastInstance hazelcastInstance, Properties properties, String mapName) {
        try {
            //
            //con = DriverManager.getConnection("jdbc:hsqldb:file:/tmp/hazelcastdb", "SA", "");

            con = DriverManager.getConnection((String) properties.get("dburl"), "sa", "");
            con.createStatement().executeUpdate(
                "create table if not exists company (id int not null, name varchar(45), primary key (id))");
            allKeysStatement = con.prepareStatement("select id from company");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override public void destroy() {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
