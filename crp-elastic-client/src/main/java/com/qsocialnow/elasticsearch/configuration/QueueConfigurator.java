package com.qsocialnow.elasticsearch.configuration;

public class QueueConfigurator {

    private final String baseDir;

    private final String queueDir;

    private final String errorQueueDir;

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
}
