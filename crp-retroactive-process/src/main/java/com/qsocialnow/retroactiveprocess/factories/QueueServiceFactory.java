package com.qsocialnow.retroactiveprocess.factories;

import com.qsocialnow.common.config.QueueConfigurator;
import com.qsocialnow.common.queues.QueueService;
import com.qsocialnow.common.queues.QueueType;

public class QueueServiceFactory {

    public static QueueService create(QueueConfigurator queueConfig) throws Exception {
        return com.qsocialnow.common.queues.QueueServiceFactory.getInstance().getQueueServiceInstance(
                QueueType.EVENTS.type(), queueConfig);
    }

}
