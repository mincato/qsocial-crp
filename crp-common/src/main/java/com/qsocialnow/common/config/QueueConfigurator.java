package com.qsocialnow.common.config;

public class QueueConfigurator implements RefreshableConfig<QueueConfigurator> {

    private static final int TOTAL_ITEM_COUNTS = 200;

    private static final int TOTAL_FAIL_ITEM_COUNTS = 400;

    private static final int TOTAL_MAX_DEAD_ITEM_COUNTS = 1200;

    private static final int DELAY = 60;

    private static final int FAIL_DELAY = 10;

    private static final int INITIAL_DELAY = 30;

    private static final String DEAD_LETTER_QUEUE_DIR = "/deadLetterQueue";

    private String baseDir;

    private String queueDir;

    private String errorQueueDir;

    private String deadLetterQueueDir = DEAD_LETTER_QUEUE_DIR;

    private int delay = DELAY;

    private int failDelay = FAIL_DELAY;

    private int initialDelay = INITIAL_DELAY;

    private int totalItemCounts = TOTAL_ITEM_COUNTS;

    private int totalFailItemCounts = TOTAL_FAIL_ITEM_COUNTS;

    private int totalMaxDeadItemCounts = TOTAL_MAX_DEAD_ITEM_COUNTS;

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

    public String getDeadLetterQueueDir() {
        return deadLetterQueueDir;
    }

    public int getDelay() {
        return delay;
    }

    public int getFailDelay() {
        return failDelay;
    }

    public int getInitialDelay() {
        return initialDelay;
    }

    public int getTotalFailItemCounts() {
        return totalFailItemCounts;
    }

    public int getTotalItemCounts() {
        return totalItemCounts;
    }

    public int getTotalMaxDeadItemCounts() {
        return totalMaxDeadItemCounts;
    }

    @Override
    public void refresh(QueueConfigurator newConfig) {
        this.baseDir = newConfig.baseDir;
        this.queueDir = newConfig.queueDir;
        this.errorQueueDir = newConfig.errorQueueDir;
        this.deadLetterQueueDir = newConfig.deadLetterQueueDir;
    }

}
