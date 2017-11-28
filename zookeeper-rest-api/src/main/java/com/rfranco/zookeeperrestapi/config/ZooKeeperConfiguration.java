package com.rfranco.zookeeperrestapi.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Created by ruben.martinez on 21/11/2017.
 */
@Configuration
public class ZooKeeperConfiguration{
    @Value("${zookeeper.connection_string}")
    String zooKeeperConnectionString;

    @Bean
    public CuratorFramework getZooKeeper() throws IOException {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zooKeeperConnectionString, retryPolicy);
        client.start();
        return client;
    }
}
