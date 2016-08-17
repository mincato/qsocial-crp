package com.qsocialnow.services.impl;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceDiscoverer {

    private ServiceDiscovery<Object> serviceDiscovery;
    private ServiceProvider<Object> serviceProvider;

    private static final Logger log = LoggerFactory.getLogger(ServiceDiscoverer.class);

    public ServiceDiscoverer(String serviceName, CuratorFramework zookeeperClient, String basePath) throws Exception {
        JsonInstanceSerializer<Object> serializer = new JsonInstanceSerializer(Object.class);
        serviceDiscovery = ServiceDiscoveryBuilder.builder(Object.class).client(zookeeperClient).basePath(basePath)
                .serializer(serializer).build();
        serviceProvider = serviceDiscovery.serviceProviderBuilder().serviceName(serviceName).build();
        serviceDiscovery.start();
        serviceProvider.start();
    }

    public ServiceInstance<Object> getServiceInstance() {
        try {
            return serviceProvider.getInstance();
        } catch (Exception e) {
            throw new RuntimeException("Error obtaining service url", e);
        }
    }

    public void close() throws Exception {
        if (serviceProvider != null) {
            log.info("Closing serviceProvider");
            serviceProvider.close();
        }
        if (serviceDiscovery != null) {
            log.info("Closing serviceDiscovery");
            serviceDiscovery.close();
        }
    }

}
