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

    private boolean stop = false;

    private QueueConsumerMonitor<T> monitor;

    private int totalItemCounts;

    private int initialDelay;

    private int delay;

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private final String type;

    private static final AtomicInteger consumingItemCount = new AtomicInteger(0);

    public QueueConsumer(final String type) {
        this.type = type;
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
            log.info("Consumer type " + this.type + " notified! Starting to read " + bigQueue.size()
                    + " elements from queue.");
            lock.notify();
        }
    };

    public abstract void process(T document);

    public abstract void save();

    public void run() {
        startMonitor();
        while (!this.stop) {
            synchronized (lock) {
                try {
                    lock.wait();
                    byte[] item = null;
                    int index = consumingItemCount.getAndIncrement();
                    while (index < getTotalItemCounts()) {
                        if (!bigQueue.isEmpty()) {
                            item = bigQueue.dequeue();
                            log.info("Consumer type " + this.type + " reading documents from queue.");
                            process(readObjectFromQueue(item));
                            index = consumingItemCount.getAndIncrement();
                        } else {
                            log.info("Consumer type " + this.type + " queue empty.");
                            break;
                        }
                    }
                    consumingItemCount.set(0);
                    save();
                } catch (Exception e) {
                    log.error("Error reading information from queue: " + this.type, e);
                } finally {
                    log.info("Consumer waiting items...");

                }
            }
        }
    }
    
    public void stopConsumer(){
    	this.stopMonitor();
    	this.stop = true;
    }
    
    private void startMonitor() {
        log.info("Starting monitor to check elements from queue - type:" + this.type);
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

    public String getType() {
        return type;
    }
}
