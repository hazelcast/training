package com.hazelcast;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class UserModel implements DataSerializable {
    private String username;
    private int userId;
    private int age;

    public UserModel() {
    }

    public UserModel(String username, int userId, int age) {
        this.username = username;
        this.userId = userId;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserModel)) {
            return false;
        }
        UserModel userModel = (UserModel) o;
        return userId == userModel.userId &&
                age == userModel.age &&
                Objects.equals(username, userModel.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, userId, age);
    }

    @Override
    public String toString() {
        return "UserModel{" + "username='" + username + '\'' + ", userId=" + userId + ", age=" + age + '}';
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(username);
        out.writeInt(userId);
        out.writeInt(age);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        // Simulating extremely slow deserialization
        sleep(5);
        username = in.readUTF();
        userId = in.readInt();
        age = in.readInt();
    }

    private void sleep(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException ignore) {
        }
    }
}
