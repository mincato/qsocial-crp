package com.qsocialnow.elasticsearch.configuration;

import com.qsocialnow.common.config.RefreshableConfig;

public class QueueConfigurator implements RefreshableConfig<QueueConfigurator> {

    private String baseDir;

    private String queueDir;

    private String errorQueueDir;

    public QueueConfigurator(final String baseDir, final String queueDir, final String errorQueueDir) {
        this.baseDir = baseDir;
        this.queueDir = queueDir;
        this.errorQueueDir = errorQueueDir;
    }

    public String getBaseDir() {
        return this.baseDir;
    }

    public String getQueueDir() {
        return this.queueDir;
    }

    public String getErrorQueueDir() {
        return errorQueueDir;
    }

    @Override
    public void refresh(QueueConfigurator newConfig) {
        this.baseDir = newConfig.baseDir;
        this.queueDir = newConfig.queueDir;
        this.errorQueueDir = newConfig.errorQueueDir;
    }
}
