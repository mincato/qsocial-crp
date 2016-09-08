package com.qsocialnow.model;

import java.util.ArrayList;
import java.util.List;

public class Series {

    private Long id;

    private String nombre;

    private List<SubSeries> subSeries = new ArrayList<SubSeries>();

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

    public List<SubSeries> getSubSeries() {
        return subSeries;
    }

    public void setSubSeries(List<SubSeries> subSeries) {
        this.subSeries = subSeries;
    }

}