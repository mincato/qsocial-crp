package com.qsocialnow.retroactiveprocess.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("appConfig")
public class RetroactiveProcessConfig {

    @Value("${app.process.znode.path}")
    private String processZnodePath;

    @Value("${app.process.request.znode.path}")
    private String processRequestZnodePath;

    @Value("${app.process.progress.znode.path}")
    private String processProgressZnodePath;

    public String getProcessZnodePath() {
        return processZnodePath;
    }

    public void setProcessZnodePath(String processZnodePath) {
        this.processZnodePath = processZnodePath;
    }

    public String getProcessRequestZnodePath() {
        return processRequestZnodePath;
    }

    public void setProcessRequestZnodePath(String processRequestZnodePath) {
        this.processRequestZnodePath = processRequestZnodePath;
    }

    public String getProcessProgressZnodePath() {
        return processProgressZnodePath;
    }

    public void setProcessProgressZnodePath(String processProgressZnodePath) {
        this.processProgressZnodePath = processProgressZnodePath;
    }

}
