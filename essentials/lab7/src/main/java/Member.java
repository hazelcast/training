import com.hazelcast.core.HazelcastInstance;

import static com.hazelcast.core.Hazelcast.newHazelcastInstance;

public class Member {
    public static void main(String[] args) {
        final HazelcastInstance hazelcastInstance = newHazelcastInstance();
    }
}
