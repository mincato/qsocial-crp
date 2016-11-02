package com.qsocialnow.service;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.retroactive.RetroactiveProcess;
import com.qsocialnow.common.model.retroactive.RetroactiveProcessEvent;
import com.qsocialnow.common.model.retroactive.RetroactiveProcessProgress;
import com.qsocialnow.common.model.retroactive.RetroactiveProcessRequest;
import com.qsocialnow.common.model.retroactive.RetroactiveProcessStatus;

@Service
public class RetroactiveService {

    private static final Logger log = LoggerFactory.getLogger(RetroactiveService.class);

    private static final Long ZERO = 0L;

    @Autowired
    private CuratorFramework zookeeperClient;

    @Value("${app.process.znode.path}")
    private String processZnodePath;

    @Value("${app.process.request.znode.path}")
    private String processRequestZnodePath;

    @Value("${app.process.progress.znode.path}")
    private String processProgressZnodePath;

    public RetroactiveProcessRequest executeNewProcess(RetroactiveProcessRequest request) {
        String requestJson = new GsonBuilder().create().toJson(request);
        try {
            zookeeperClient.setData().forPath(processRequestZnodePath, requestJson.getBytes());
            RetroactiveProcessProgress retroactiveProcessProgress = new RetroactiveProcessProgress();
            retroactiveProcessProgress.setStatus(RetroactiveProcessStatus.START);
            retroactiveProcessProgress.setEventsProcessed(ZERO);
            String progress = new GsonBuilder().create().toJson(retroactiveProcessProgress);
            zookeeperClient.setData().forPath(processProgressZnodePath, progress.getBytes());
            zookeeperClient.setData().forPath(processZnodePath, RetroactiveProcessEvent.START.name().getBytes());
        } catch (Exception e) {
            log.error("There was an error updating zookeeper to start a new retroactive process", e);
            throw new RuntimeException(e);
        }
        return request;
    }

    public RetroactiveProcess getProcess() {
        RetroactiveProcess process = new RetroactiveProcess();
        process.setProgress(getProgress());
        process.setRequest(getRequest());
        return process;
    }

    public void cancelProcess() {
        try {
            zookeeperClient.setData().forPath(processZnodePath, RetroactiveProcessEvent.STOP.name().getBytes());
        } catch (Exception e) {
            log.error("There was an error updating zookeeper to stop current process", e);
            throw new RuntimeException(e);
        }
    }

    private RetroactiveProcessRequest getRequest() {
        RetroactiveProcessRequest request = null;
        try {
            byte[] requestBytes = zookeeperClient.getData().forPath(processRequestZnodePath);
            if (requestBytes != null) {
                request = new GsonBuilder().create()
                        .fromJson(new String(requestBytes), RetroactiveProcessRequest.class);
            }
        } catch (Exception e) {
            log.error("There was an error getting retroactive process request", e);
            throw new RuntimeException(e);
        }
        return request;
    }

    private RetroactiveProcessProgress getProgress() {
        RetroactiveProcessProgress progress = null;
        try {
            byte[] progressBytes = zookeeperClient.getData().forPath(processProgressZnodePath);
            if (progressBytes != null) {
                progress = new GsonBuilder().create().fromJson(new String(progressBytes),
                        RetroactiveProcessProgress.class);
            }
        } catch (Exception e) {
            log.error("There was an error getting retroactive process progress", e);
            throw new RuntimeException(e);
        }
        return progress;
    }
}
