package com.qsocialnow;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

public class ServiceDiscoveryRegister {

    private static final String SERVICE_NAME = "ticket-manager-service";

    private static final Logger log = LoggerFactory.getLogger(ServiceDiscoveryRegister.class);

    @Autowired
    @Qualifier("zookeeperCentralClient")
    private CuratorFramework zookeeperClient;

    @Value("${app.service.base.path}")
    private String basePath;

    @Value("${SERVER_ADDRESS:localhost}")
    private String host;

    private ServiceDiscovery<Object> serviceDiscovery;

    public void register(int servicePort) throws Exception {
        UriSpec uriSpec = new UriSpec("{scheme}://{address}:{port}/crp-ticket-manager-service/services");
        ServiceInstance<Object> thisInstance = ServiceInstance.builder().name(SERVICE_NAME).uriSpec(uriSpec)
                .address(host).port(servicePort).build();
        serviceDiscovery = ServiceDiscoveryBuilder.builder(Object.class).client(zookeeperClient).basePath(basePath)
                .serializer(new JsonInstanceSerializer<>(Object.class)).thisInstance(thisInstance).build();
        serviceDiscovery.start();
    }

    public void close() {
        try {
            if (serviceDiscovery != null) {
                serviceDiscovery.close();
            }
        } catch (Exception e) {
            log.error("Error Closing Discovery", e);
        }
    }

}
