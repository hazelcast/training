import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

// https://github.com/hazelcast/hazelcast/issues/9081

public class XMLMain {
    public static void main(String[] args) {
    	HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        HazelcastInstance client = HazelcastClient.newHazelcastClient();
    }
}
