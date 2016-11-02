package data;

import java.io.Serializable;

public class Person implements Serializable {

    public Long id;
    public String name;

    public Person() {
    }

    public Person(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String toString() {
        return "Person{name='" + name + "'}";
    }
}
