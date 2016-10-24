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
public class RetroactiveProcessor implements Runnable{

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
    
    private boolean shutdown;
    
    private Object lock = new Object();
    
	@Override
	public void run() {
		synchronized (lock) {
			while(!running && !shutdown) {
				try {
					lock.wait();
					if (!shutdown) {						
						runProcess();
						running = false;
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
            log.info("starting new process");
            long eventsProcessed = 0L;
            RetroactiveProcessProgress retroactiveProcessProgress = initProcess(eventsProcessed);
            int i = 0;
            EventsRetroactiveService retroactiveService = eventsRetroactiveServiceBuilder.build(request);
            EventsPaginatedResponse bean = null;
            do {
                try {
                    log.info("processing page: " + i);
                    bean = retroactiveService.buildResponse(request);
                    if (bean != null && bean.getEvents() != null) {
                        for (String event : bean.getEvents()) {
                            // usar bigqueue
                            kafkaProducer.send(event);
                        }
                        eventsProcessed += bean.getEvents().size();
                        request.setScrollId(bean.getScrollId());
                        retroactiveProcessProgress.setEventsProcessed(eventsProcessed);
                        updateProgress(retroactiveProcessProgress);
                        i++;
                        Thread.sleep(60000);
                    }
                } catch (Throwable e) {
                    log.error(
                            String.format("There was an error processing page %s with scroll id %s", i,
                                    request.getScrollId()), e);
                }
            } while (!stop && bean != null && bean.getScrollId() != null);
            if (!stop) {
                retroactiveProcessProgress.setStatus(RetroactiveProcessStatus.FINISH);
                updateProgress(retroactiveProcessProgress);
            }
        } else {
            log.error("There is no request to process");
        }
	}


	public void start() {
		if (!running) {
			synchronized (lock) {
				running = true;
				stop = false;
				lock.notify();
			}
		} else {
			log.warn("there is a process running already");
		}

    }

    private RetroactiveProcessProgress initProcess(long eventsProcessed) {
        RetroactiveProcessProgress retroactiveProcessProgress = new RetroactiveProcessProgress();
        retroactiveProcessProgress.setStatus(RetroactiveProcessStatus.PROCESSING);
        retroactiveProcessProgress.setEventsProcessed(eventsProcessed);
        updateProgress(retroactiveProcessProgress);
        return retroactiveProcessProgress;
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

    private RealTimeReportBean buildRequest() {
        RealTimeReportBean request = null;
        try {
            byte[] requestBytes = zookeeperClient.getData().forPath(appConfig.getProcessRequestZnodePath());
            if (ArrayUtils.isNotEmpty(requestBytes)) {
                request = new GsonBuilder().create().fromJson(new String(requestBytes), RealTimeReportBean.class);
                request.setScrollExpirationDuration(pagedEventsServiceConfig.getScrollExpirationDuration());
                request.setScrollId(null);
            }
        } catch (Exception e) {
            log.error("There was an error building request", e);
        }
        return request;
    }

    public void resume() {
        if (!running) {
            running = true;
            log.info("resuming process");
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
    
    public void shutdown () {
    	stop();
    	synchronized (lock) {
    		shutdown = true;
    		lock.notify();
		}
    }

}
