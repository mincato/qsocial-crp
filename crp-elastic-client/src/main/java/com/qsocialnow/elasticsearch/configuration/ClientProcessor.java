package com.qsocialnow.elasticsearch.configuration;

import java.util.Timer;

public class ClientProcessor {

    public ClientProcessor() {

    }

    public static void executeCommand(final String[] commands) {

        ProducerTask mTask = new ProducerTask();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(mTask, 0, 5000);
    }

}
