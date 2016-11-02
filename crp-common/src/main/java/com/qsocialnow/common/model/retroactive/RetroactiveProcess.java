package com.qsocialnow.common.model.retroactive;

public class RetroactiveProcess {

    public RetroactiveProcessProgress progress;

    public RetroactiveProcessRequest request;

    public RetroactiveProcessProgress getProgress() {
        return progress;
    }

    public void setProgress(RetroactiveProcessProgress progress) {
        this.progress = progress;
    }

    public RetroactiveProcessRequest getRequest() {
        return request;
    }

    public void setRequest(RetroactiveProcessRequest request) {
        this.request = request;
    }

}
