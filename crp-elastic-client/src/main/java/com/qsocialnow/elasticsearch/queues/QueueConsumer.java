package com.qsocialnow.elasticsearch.queues;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leansoft.bigqueue.IBigQueue;

public abstract class QueueConsumer<T> extends Thread {

    private static final Logger log = LoggerFactory.getLogger(QueueConsumer.class);

    private IBigQueue bigQueue;

    private Object lock = new Object();

    private QueueConsumerMonitor<T> monitor;

    private int totalItemCounts;

    private int initialDelay;

    private int delay;

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private static final AtomicInteger consumingItemCount = new AtomicInteger(0);

    public QueueConsumer() {
        monitor = new QueueConsumerMonitor<T>(this);
    }

    public void setQueue(IBigQueue bigQueue) {
        this.bigQueue = bigQueue;
    }

    @SuppressWarnings("unchecked")
    public T readObjectFromQueue(byte[] item) {
        ByteArrayInputStream bis = new ByteArrayInputStream(item);
        ObjectInput in = null;
        T result = null;
        try {
            in = new ObjectInputStream(bis);
            result = (T) in.readObject();
        } catch (IOException ex) {
            log.error("Error converting object information from queue", ex);
        } catch (ClassNotFoundException e) {
            log.error("Error reading object information from queue", e);
        } finally {
            try {
                bis.close();
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                log.error("Error closing stream", ex);
            }
        }
        return result;
    }

    public synchronized void nofityQueueReady() {
        synchronized (lock) {
            log.info("Consumer notified starting to read from queue.");
            lock.notify();
        }
    };

    public abstract void process(T document);

    public abstract void save();

    public void run() {
        startMonitor();
        while (true) {
            synchronized (lock) {
                try {
                    log.info("Starting consumer..");
                    lock.wait();
                    log.info("Starting to read..");
                    byte[] item = null;
                    int index = consumingItemCount.getAndIncrement();
                    while (index < getTotalItemCounts()) {
                        if (!bigQueue.isEmpty()) {
                            item = bigQueue.dequeue();
                            process(readObjectFromQueue(item));
                            index = consumingItemCount.getAndIncrement();
                        } else {
                            break;
                        }
                    }
                    log.info("Finish to read the queue");
                    consumingItemCount.set(0);
                    save();
                } catch (Exception e) {
                    log.error("Error reading information from queue", e);
                } finally {
                    log.info("Consumer waiting items...");

                }
            }
        }
    }

    private void startMonitor() {
        log.info("Starting monitor...");
        executor.scheduleWithFixedDelay(monitor, getInitialDelay(), getDelay(), TimeUnit.MINUTES);
    }

    private void stopMonitor() {
        if (!executor.isTerminated()) {
            log.info("Stoping consumer monitor");
        }
        executor.shutdownNow();
    }

    public int getTotalItemCounts() {
        return totalItemCounts;
    }

    public void setTotalItemCounts(int totalItemCounts) {
        this.totalItemCounts = totalItemCounts;
    }

    public int getInitialDelay() {
        return initialDelay;
    }

    public void setInitialDelay(int initialDelay) {
        this.initialDelay = initialDelay;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
