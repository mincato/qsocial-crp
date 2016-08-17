package com.qsocialnow.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZookeeperExecutor {

    private static final Logger log = LoggerFactory.getLogger(ZookeeperExecutor.class);

    private final ZooKeeper zk;

    public ZookeeperExecutor(ZooKeeper zookeeper) {
        this.zk = zookeeper;
    }

    public String getData(String path) {
        try {
            Stat stat = exists(path);
            if (stat != null) {
                return new String(zk.getData(path, false, null));
            } else {
                log.info(String.format("Node not exists: %s", path));
            }
        } catch (KeeperException e) {
            log.error("Unexpected error", e);
        } catch (InterruptedException e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

    private Stat exists(String path) throws KeeperException, InterruptedException {
        return zk.exists(path, true);
    }

}
