package com.qsocialnow.common.queues;

import java.util.HashMap;
import java.util.Map;

import com.qsocialnow.common.config.QueueConfigurator;

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

    public QueueService getQueueServiceInstance(String queueType, QueueConfigurator configurator) {
        QueueService service = queueServicesMap.get(queueType);
        if (service == null) {
            service = new QueueService(configurator);
            if (service.initQueue(queueType)) {
                if (service.initFailQueue(queueType)) {
                    queueServicesMap.put(queueType, service);
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
