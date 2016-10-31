package com.qsocialnow.retroactiveprocess.processors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.GsonBuilder;
import com.odatech.microservices.request.RealTimeReportBean;
import com.odatech.microservices.response.EventsPaginatedResponse;
import com.qsocialnow.common.model.retroactive.RetroactiveProcessProgress;
import com.qsocialnow.common.model.retroactive.RetroactiveProcessStatus;
import com.qsocialnow.kafka.producer.Producer;
import com.qsocialnow.retroactiveprocess.builder.EventsRetroactiveServiceBuilder;
import com.qsocialnow.retroactiveprocess.config.PagedEventsServiceConfig;
import com.qsocialnow.retroactiveprocess.config.RetroactiveProcessConfig;
import com.qsocialnow.retroactiveprocess.service.EventsRetroactiveService;

@Service
public class RetroactiveProcessor implements Runnable {

    private static final Long ZERO = 0L;

    private static Logger log = LoggerFactory.getLogger(RetroactiveProcessor.class);

    @Autowired
    private CuratorFramework zookeeperClient;

    @Autowired
    private RetroactiveProcessConfig appConfig;

    @Autowired
    private PagedEventsServiceConfig pagedEventsServiceConfig;

    @Autowired
    private EventsRetroactiveServiceBuilder eventsRetroactiveServiceBuilder;

    @Autowired
    private Producer kafkaProducer;

    private boolean running;

    private boolean stop;

    private boolean stopByShutdown;

    private boolean shutdown;

    private boolean resume;

    private Object lock = new Object();

    @Override
    public void run() {
        synchronized (lock) {
            while (!running && !shutdown) {
                try {
                    lock.wait();
                    if (!shutdown) {
                        runProcess();
                        running = false;
                        resume = false;
                    }
                } catch (InterruptedException e) {
                    log.error("There was an unexpected error", e);
                }
            }
        }
    }

    private void runProcess() {
        RealTimeReportBean request = buildRequest();
        if (request != null) {
            RetroactiveProcessProgress retroactiveProcessProgress = initProcess();
            if (retroactiveProcessProgress != null) {
                log.info("starting process");
                long eventsProcessed = retroactiveProcessProgress.getEventsProcessed();
                request.setScrollId(retroactiveProcessProgress.getScrollId());
                EventsRetroactiveService retroactiveService = eventsRetroactiveServiceBuilder.build(request);
                EventsPaginatedResponse bean = null;
                try {
                    do {
                        log.info("processing page with scroll id: " + request.getScrollId());
                        bean = retroactiveService.buildResponse(request);
                        if (bean != null && bean.getEvents() != null) {
                            for (String event : bean.getEvents()) {
                                // usar bigqueue
                                kafkaProducer.send(event);
                            }
                            eventsProcessed += bean.getEvents().size();
                            retroactiveProcessProgress.setEventsProcessed(eventsProcessed);
                            retroactiveProcessProgress.setScrollId(bean.getScrollId());
                            updateProgress(retroactiveProcessProgress);
                            log.info(String.format("Page with scroll id %s processed successfully",
                                    request.getScrollId()));
                            request.setScrollId(bean.getScrollId());
                        }
                    } while (!stop && bean != null && bean.getScrollId() != null);
                    if (!stopByShutdown) {
                        if (stop) {
                            retroactiveProcessProgress.setStatus(RetroactiveProcessStatus.STOP);
                        } else {
                            retroactiveProcessProgress.setStatus(RetroactiveProcessStatus.FINISH);
                        }
                        updateProgress(retroactiveProcessProgress);
                    }
                } catch (Throwable e) {
                    log.error(String.format("There was an error processing page with scroll id %s",
                            request.getScrollId()), e);
                    retroactiveProcessProgress.setStatus(RetroactiveProcessStatus.ERROR);
                    retroactiveProcessProgress.setErrorMessage(e.getMessage());
                    updateProgress(retroactiveProcessProgress);
                }
            }
        } else {
            log.error("There is no request to process");
        }
    }

    public void start() {
        if (!running) {
            synchronized (lock) {
                running = true;
                resume = false;
                stop = false;
                lock.notify();
            }
        } else {
            log.warn("there is a process running already");
        }

    }

    private RetroactiveProcessProgress initProcess() {
        RetroactiveProcessProgress retroactiveProcessProgress = null;
        if (resume) {
            retroactiveProcessProgress = getProgress();
            if (!checkNeedsToResume(retroactiveProcessProgress)) {
                if (retroactiveProcessProgress != null
                        && RetroactiveProcessStatus.START.equals(retroactiveProcessProgress.getStatus())) {
                    retroactiveProcessProgress = initialProcessProgress();
                } else {
                    retroactiveProcessProgress = null;
                }
            }
        } else {
            retroactiveProcessProgress = initialProcessProgress();
        }
        return retroactiveProcessProgress;
    }

    private RetroactiveProcessProgress initialProcessProgress() {
        log.info("Initializing a new process");
        RetroactiveProcessProgress retroactiveProcessProgress = new RetroactiveProcessProgress();
        retroactiveProcessProgress.setStatus(RetroactiveProcessStatus.PROCESSING);
        retroactiveProcessProgress.setEventsProcessed(ZERO);
        updateProgress(retroactiveProcessProgress);
        return retroactiveProcessProgress;
    }

    private boolean checkNeedsToResume(RetroactiveProcessProgress retroactiveProcessProgress) {
        if (retroactiveProcessProgress != null
                && (RetroactiveProcessStatus.PROCESSING.equals(retroactiveProcessProgress.getStatus()) || RetroactiveProcessStatus.START
                        .equals(retroactiveProcessProgress.getStatus()))) {
            if (retroactiveProcessProgress.getScrollId() != null
                    || ZERO.equals(retroactiveProcessProgress.getEventsProcessed())) {
                return true;
            } else {
                log.info("The process have already finished. Updating process to finish.");
                retroactiveProcessProgress.setStatus(RetroactiveProcessStatus.FINISH);
                updateProgress(retroactiveProcessProgress);
                return false;
            }
        }
        log.info("The process does not need to resume");
        return false;
    }

    private void updateProgress(RetroactiveProcessProgress retroactiveProcessProgress) {
        try {
            String progress = new GsonBuilder().create().toJson(retroactiveProcessProgress);
            zookeeperClient.setData().inBackground()
                    .forPath(appConfig.getProcessProgressZnodePath(), progress.getBytes());
        } catch (Exception e) {
            log.error("There was an error updating progress", e);
        }

    }

    private RetroactiveProcessProgress getProgress() {
        try {
            byte[] progressBytes = zookeeperClient.getData().forPath(appConfig.getProcessProgressZnodePath());
            if (progressBytes != null) {
                return new GsonBuilder().create().fromJson(new String(progressBytes), RetroactiveProcessProgress.class);
            }
        } catch (Exception e) {
            log.error("There was an error retrieving progress", e);
        }
        return null;
    }

    private RealTimeReportBean buildRequest() {
        RealTimeReportBean request = null;
        try {
            byte[] requestBytes = zookeeperClient.getData().forPath(appConfig.getProcessRequestZnodePath());
            if (ArrayUtils.isNotEmpty(requestBytes)) {
                request = new GsonBuilder().create().fromJson(new String(requestBytes), RealTimeReportBean.class);
                request.setScrollExpirationDuration(pagedEventsServiceConfig.getScrollExpirationDuration());
                request.setMaxResults(pagedEventsServiceConfig.getMaxResults());
            }
        } catch (Exception e) {
            log.error("There was an error building request", e);
        }
        return request;
    }

    public void resume() {
        if (!running) {
            synchronized (lock) {
                log.info("resuming process");
                running = true;
                resume = true;
                stop = false;
                lock.notify();
            }
        } else {
            log.warn("there is a process running already");
        }

    }

    public void stop() {
        if (running) {
            stop = true;
            log.info("stoping process");
        } else {
            log.warn("there is no process running right now");
        }
    }

    public void shutdown() {
        stop();
        stopByShutdown = true;
        synchronized (lock) {
            shutdown = true;
            lock.notify();
        }
    }

}
