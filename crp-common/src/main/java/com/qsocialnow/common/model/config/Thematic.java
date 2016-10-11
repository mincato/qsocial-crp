package com.qsocialnow.common.model.config;

import java.util.ArrayList;
import java.util.List;

public class Thematic {

    private Long id;

    private String nombre;

    private Boolean running;

    private List<Series> series = new ArrayList<Series>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getRunning() {
        return running;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }

    public List<Series> getSeries() {
        return series;
    }

    public void setSeries(List<Series> series) {
        this.series = series;
    }

}
