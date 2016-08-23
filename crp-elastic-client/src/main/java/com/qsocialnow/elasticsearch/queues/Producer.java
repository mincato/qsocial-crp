package com.qsocialnow.elasticsearch.queues;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leansoft.bigqueue.IBigQueue;

public class Producer<T> extends Thread {

    private static final Logger log = LoggerFactory.getLogger(Producer.class);

    private static final int TOTAL_ITEM_COUNTS = 3;

    private IBigQueue bigQueue;

    private List<Consumer<T>> consumers;

    private static final AtomicInteger producingItemCount = new AtomicInteger(0);

    public Producer() {
        consumers = new ArrayList<Consumer<T>>();
    }

    public void addItem(T document) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(document);
            byte[] item = bos.toByteArray();

            bigQueue.enqueue(item);
            producingItemCount.incrementAndGet();
            log.info("Adding item into the queue");

            if (producingItemCount.get() >= TOTAL_ITEM_COUNTS) {
                this.notifyConsumers();
                log.info("Finish to write the queue");
                producingItemCount.set(0);
            }
        } catch (IOException ex) {
            log.error("Error trying to serealize case:" + ex);
        } finally {
            try {
                if (out != null) {
                    out.close();
                    bos.close();
                }
            } catch (IOException ex) {
                log.error("Error closing stream");
            }
        }

    }

    public void notifyConsumers() {
        for (Consumer<T> consumer : consumers) {
            log.info("Notifying consumers...");
            consumer.nofityQueueReady();
        }
    }

    public void setQueue(IBigQueue bigQueue) {
        this.bigQueue = bigQueue;
    }

    public void addConsumer(Consumer<T> consumer) {
        this.consumers.add(consumer);
    }

    public void run() {
        try {
            while (true) {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
