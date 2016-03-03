import java.io.Serializable;

public class Person implements Serializable {
    public String name;
    public boolean active;
    public int age;

    public Person(String name, boolean active, int age) {
        this.active = active;
        this.age = age;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "active=" + active +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
