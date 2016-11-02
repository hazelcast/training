package jdbc;

import com.google.common.collect.Sets;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MapLoaderLifecycleSupport;
import com.hazelcast.core.MapStore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static java.lang.String.format;

import data.Person;

public class PersonMapStore implements MapStore<Long, Person>, MapLoaderLifecycleSupport {

    private Connection con;
    private PreparedStatement allKeysStatement;

    public PersonMapStore() {

    }

    public synchronized void delete(Long key) {
        System.out.println("Delete:" + key);
        try {
            con.createStatement().executeUpdate(
                    format("delete from person where id = %s", key));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void store(Long key, Person value) {
        try {
            con.createStatement().executeUpdate(
                    format("insert into person values(%s,'%s')", key, value.name));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void storeAll(Map<Long, Person> map) {
        for (Map.Entry<Long, Person> entry : map.entrySet())
            store(entry.getKey(), entry.getValue());
    }

    public synchronized void deleteAll(Collection<Long> keys) {
        for (Long key : keys) delete(key);
    }

    public synchronized Person load(Long key) {
        try {
            ResultSet resultSet = con.createStatement().executeQuery(
                    format("select name from person where id =%s", key));
            try {
                if (!resultSet.next()) return null;
                String name = resultSet.getString(1);
                return new Person(key, name);
            } finally {
                resultSet.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized Map<Long, Person> loadAll(Collection<Long> keys) {
        Map<Long, Person> result = new HashMap<Long, Person>();
        for (Long key : keys) result.put(key, load(key));
        return result;
    }

    public Set<Long> loadAllKeys() {
        StatementIterable<Long> keys = new StatementIterable<Long>(allKeysStatement);
        return Sets.newHashSet(keys);
    }

    @Override
    public void init(HazelcastInstance hazelcastInstance, Properties properties, String mapName) {
        try {
            con = DriverManager.getConnection("jdbc:hsqldb:file:testdb", "SA", "");
            con.createStatement().executeUpdate(
                    "create table if not exists person (id bigint not null, name varchar(45), primary key (id))");
            allKeysStatement = con.prepareStatement("select id from person");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void destroy() {
        try {
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
