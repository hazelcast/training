package com.hazelcast.techops.training.bootcamp;

import com.hazelcast.config.Config;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.techops.training.bootcamp.util.LicenseUtil;

/**
 * Main class for creating server instance in Cluster A.
 *
 */

public class WanReplicationClusterA {

    public static void main(String[] args) {
        Config config = new Config();
        config.setLicenseKey(LicenseUtil.KEY);
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(true);
        config.getNetworkConfig().setPort(5701).setPortAutoIncrement(true).setPortCount(20);
        config.getNetworkConfig().getJoin().getTcpIpConfig().addMember("127.0.0.1");

        GroupConfig groupConfig = new GroupConfig();
        groupConfig.setName("ClusterA-Name");
        groupConfig.setPassword("ClusterA-Pass");
        config.setGroupConfig(groupConfig);

        config.addMapConfig(buildMapConfig());

        Hazelcast.newHazelcastInstance(config);
    }

    private static MapConfig buildMapConfig() {
        MapConfig mapConfig = new MapConfig("WAN_MAP");
        mapConfig.setMaxIdleSeconds(120);
        return mapConfig;
    }

}
