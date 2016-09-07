package com.qsocialnow.elasticsearch.queues;

import java.util.HashMap;
import java.util.Map;

import com.qsocialnow.elasticsearch.configuration.QueueConfigurator;

public class QueueServiceFactory {

    private static Map<String, QueueService> queueServicesMap;

    private static QueueServiceFactory instance;

    private QueueServiceFactory() {
        queueServicesMap = new HashMap<String, QueueService>();
    }

    public static QueueServiceFactory getInstance() {
        if (instance == null)
            instance = new QueueServiceFactory();

        return instance;
    }

    public QueueService getQueueServiceInstance(QueueType queueType, QueueConfigurator configurator) {
        QueueService service = queueServicesMap.get(queueType.type());
        if (service == null) {
            service = new QueueService(configurator);
            if (service.initQueue(queueType.type())) {
                if (service.initFailQueue(queueType.type())) {
                    queueServicesMap.put(queueType.type(), service);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
        return service;
    }

}
