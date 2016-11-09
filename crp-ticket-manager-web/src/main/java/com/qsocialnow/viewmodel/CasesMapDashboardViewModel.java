package com.qsocialnow.viewmodel;

import java.io.Serializable;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.NotifyCommand;
import org.zkoss.bind.annotation.ToClientCommand;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.services.CaseService;

@NotifyCommand(value = "zmapbox$clientUpdate", onChange = "_vm_.geoJson")
@ToClientCommand({ "zmapbox$clientUpdate" })
@VariableResolver(DelegatingVariableResolver.class)
public class CasesMapDashboardViewModel implements Serializable {

    private static final long serialVersionUID = 4490965082748427009L;

    private Integer minZoom = 2;

    private Integer maxZoom = 15;

    private Integer zoom = 3;

    private Double initialPointLat;

    private Double initialPointLng;

    private boolean cluster = true;

    private String data;

    private String geoJson;

    @WireVariable
    private CaseService caseService;

    @Init
    public void init() {
        initialPointLat = 37.8;
        initialPointLng = -96.0;
    }

    public Integer getMaxZoom() {
        return maxZoom;
    }

    public void setMaxZoom(Integer maxZoom) {
        this.maxZoom = maxZoom;
    }

    public Integer getMinZoom() {
        return minZoom;
    }

    public void setMinZoom(Integer minZoom) {
        this.minZoom = minZoom;
    }

    public Double getInitialPointLat() {
        return initialPointLat;
    }

    public void setInitialPointLat(Double initialPointLat) {
        this.initialPointLat = initialPointLat;
    }

    public Double getInitialPointLng() {
        return initialPointLng;
    }

    public void setInitialPointLng(Double initialPointLng) {
        this.initialPointLng = initialPointLng;
    }

    public Integer getZoom() {
        return zoom;
    }

    public void setZoom(Integer zoom) {
        this.zoom = zoom;
    }

    public boolean isCluster() {
        return cluster;
    }

    public void setCluster(boolean cluster) {
        this.cluster = cluster;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Command
    @NotifyChange("geoJson")
    public void update() {
        geoJson = caseService.findGeoJson();
    }

    public String getGeoJson() {
        return geoJson;
    }

    public void setGeoJson(String geoJson) {
        this.geoJson = geoJson;
    }

}