package com.qsocialnow.retroactiveprocess.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RealTimeStaticServerByThematic {

    private Long id;

    private String realTimeStatisticsServer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRealTimeStatisticsServer() {
        return realTimeStatisticsServer;
    }

    public void setRealTimeStatisticsServer(String realTimeStatisticsServer) {
        this.realTimeStatisticsServer = realTimeStatisticsServer;
    }

}
