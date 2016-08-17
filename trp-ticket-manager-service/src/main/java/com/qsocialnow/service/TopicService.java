package com.qsocialnow.service;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.qsocialnow.kafka.admin.Administrator;

@Service
public class TopicService {

    private Logger log = LoggerFactory.getLogger(TopicService.class);

    @Autowired
    private Administrator administrator;

    @Value("${app.kafka.zookeeper.host.path}")
    private String kafkaZookeeerZnodePath;

    @Autowired
    @Qualifier("zookeeperClient")
    private CuratorFramework zookeeperClient;

    public void create(String domainName) {
        String topicName = "prc.".concat(domainName);
        try {
            String kafkaZookeeperHost = new String(zookeeperClient.getData().forPath(kafkaZookeeerZnodePath));
            log.info(String.format("Creando topic: %s", topicName));
            administrator.createTopic(kafkaZookeeperHost, topicName);
        } catch (Exception ex) {
            log.error("Unexpected error", ex);
        }
    }

}
