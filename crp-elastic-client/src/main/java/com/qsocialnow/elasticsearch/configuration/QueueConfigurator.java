package com.qsocialnow.elasticsearch.configuration;

public class QueueConfigurator {

    private final String baseDir;

    private final String queueDir;

    public QueueConfigurator() {
        this.baseDir = "/tmp/bigqueue/";
        this.queueDir = this.baseDir + "qsocial";
    }

    public QueueConfigurator(final String baseDir, final String queueDir) {
        this.baseDir = baseDir;
        this.queueDir = queueDir;
    }

    public String getBaseDir() {
        return this.baseDir;
    }

    public String getQueueDir() {
        return this.queueDir;
    }
}
