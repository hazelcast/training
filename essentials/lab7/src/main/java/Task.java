import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;

import java.io.Serializable;
import java.time.LocalDate;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Task implements Runnable, Serializable, HazelcastInstanceAware {
    private transient HazelcastInstance hazelcastInstance;

    public void run() {
        while (true) {
            final com.hazelcast.core.Member localMember = this.hazelcastInstance.getCluster().getLocalMember();
            final String uuid = localMember.getUuid();
            final IMap<Object, Object> data = hazelcastInstance.getMap("data");
            final String uuid2 = hazelcastInstance.getPartitionService().getPartition("key1").getOwner().getUuid();
            System.out.println("Member's " + uuid + " Time: " + LocalDate.now());
            System.out.println("expected uuid: " + uuid2);
            try {
                SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            localMember.getIntAttribute("hazelcast.my.tasks");
        }
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
