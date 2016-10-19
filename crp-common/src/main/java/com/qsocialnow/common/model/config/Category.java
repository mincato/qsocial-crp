package com.qsocialnow.common.model.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Category extends NameByLanguage {

    private Long id;

    private String descripcion;

    private Long idCategoriaOpuesta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getIdCategoriaOpuesta() {
        return idCategoriaOpuesta;
    }

    public void setIdCategoriaOpuesta(Long idCategoriaOpuesta) {
        this.idCategoriaOpuesta = idCategoriaOpuesta;
    }

}
