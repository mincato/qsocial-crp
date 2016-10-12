package com.qsocialnow.common.model.config;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryGroup {

    private Long id;

    private Integer numeroDeOrden;

    private String descripcion;

    private String nombre;

    private String iconoHabilitado;

    private String iconoDesHabilitado;

    private List<NameByLanguage> nombrePorIdioma = new ArrayList<NameByLanguage>();

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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public List<NameByLanguage> getNombrePorIdioma() {
        return nombrePorIdioma;
    }

    public void setNombrePorIdioma(List<NameByLanguage> nombrePorIdioma) {
        this.nombrePorIdioma = nombrePorIdioma;
    }

    public List<Category> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Category> categorias) {
        this.categorias = categorias;
    }

}
