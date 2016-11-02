import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.IMap;
import com.hazelcast.durableexecutor.DurableExecutorService;

public class Client {
    public static void main(String[] args) {
        final HazelcastInstance hazelcastClient = HazelcastClient.newHazelcastClient();

        final DurableExecutorService jobs = hazelcastClient.getDurableExecutorService("jobs");
        final IMap<Object, Object> data = hazelcastClient.getMap("data");
        data.put("key1", "data");
        //final Task runnable = new Task();

        jobs.submitToKeyOwner(new Task(), "key1");

        final IExecutorService e = hazelcastClient.getExecutorService("e");
        e.execute(new Task());
    }
}
