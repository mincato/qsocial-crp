package com.qsocialnow.common.model.config;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryGroup extends NameByLanguage {

    private Long id;

    private Integer numeroDeOrden;

    private String descripcion;

    private String iconoHabilitado;

    private String iconoDesHabilitado;

    private List<Category> categorias = new ArrayList<Category>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroDeOrden() {
        return numeroDeOrden;
    }

    public void setNumeroDeOrden(Integer numeroDeOrden) {
        this.numeroDeOrden = numeroDeOrden;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIconoHabilitado() {
        return iconoHabilitado;
    }

    public void setIconoHabilitado(String iconoHabilitado) {
        this.iconoHabilitado = iconoHabilitado;
    }

    public String getIconoDesHabilitado() {
        return iconoDesHabilitado;
    }

    public void setIconoDesHabilitado(String iconoDesHabilitado) {
        this.iconoDesHabilitado = iconoDesHabilitado;
    }

    public List<Category> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Category> categorias) {
        this.categorias = categorias;
    }

}
