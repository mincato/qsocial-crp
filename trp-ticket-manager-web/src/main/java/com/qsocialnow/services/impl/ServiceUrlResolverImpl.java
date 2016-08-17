package com.qsocialnow.services.impl;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.qsocialnow.services.ServiceUrlResolver;

@Service("serviceUrlResolver")
public class ServiceUrlResolverImpl implements ServiceUrlResolver {

    private static final Logger log = LoggerFactory.getLogger(ServiceUrlResolverImpl.class);

    @Value("${zookeeper.services.base.path}")
    private String servicesBasePath;

    @Autowired
    @Qualifier("zookeeperClient")
    private CuratorFramework zookeeperClient;

    private Map<String, ServiceDiscoverer> serviceDiscoverersByClients;

    public ServiceUrlResolverImpl() {
        serviceDiscoverersByClients = new ConcurrentHashMap<>();
    }

    @Override
    public String resolveUrl(String clientName, String serviceUrlPath) throws Exception {
        initServiceDiscover(clientName);
        ServiceInstance<Object> serviceInstance = serviceDiscoverersByClients.get(clientName).getServiceInstance();
        if (serviceInstance == null) {
            throw new Exception(String.format("There is no service available for client: %s", clientName));
        }
        String serviceHost = serviceInstance.buildUriSpec();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serviceHost).path(serviceUrlPath);
        return builder.toUriString();
    }

    private synchronized void initServiceDiscover(String clientName) throws Exception {
        if (!serviceDiscoverersByClients.containsKey(clientName)) {
            serviceDiscoverersByClients.put(clientName, new ServiceDiscoverer("ticket-manager-service",
                    zookeeperClient, MessageFormat.format(servicesBasePath, clientName)));
        }
    }

    @PreDestroy
    public void close() throws Exception {
        log.info("Closing serviceUrlResolverImpl");
        for (ServiceDiscoverer serviceDiscoverer : serviceDiscoverersByClients.values()) {
            serviceDiscoverer.close();
        }
    }

}
