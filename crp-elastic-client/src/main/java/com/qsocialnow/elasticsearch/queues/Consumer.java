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

public abstract class Consumer<T> extends Thread {

    private static final int DELAY = 1;

	private static final int INITIAL_DELAY = 1;

	private static final Logger log = LoggerFactory.getLogger(Consumer.class);

    private static final int TOTAL_ITEM_COUNTS = 3;

    private IBigQueue bigQueue;

    private Object lock = new Object();

    private ConsumerMonitor<T> monitor;

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private static final AtomicInteger consumingItemCount = new AtomicInteger(0);

    public Consumer() {
        monitor = new ConsumerMonitor<T>(this);
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
            log.info("Consumer notified ready to read..");
            lock.notify();
        }
    };

    public abstract void addDocument(T document);

    public abstract void saveDocuments();

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
                    while (index < TOTAL_ITEM_COUNTS) {
                        if (!bigQueue.isEmpty()) {
                            item = bigQueue.dequeue();
                            addDocument(readObjectFromQueue(item));
                            index = consumingItemCount.getAndIncrement();
                        } else {
                            break;
                        }
                    }
                    log.info("Finish to read the queue");
                    consumingItemCount.set(0);
                    saveDocuments();
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
        executor.scheduleWithFixedDelay(monitor, INITIAL_DELAY, DELAY, TimeUnit.MINUTES);
    }

    private void stopMonitor() {
        if (!executor.isTerminated()) {
            log.info("Stoping consumer monitor");
        }
        executor.shutdownNow();
    }
}
