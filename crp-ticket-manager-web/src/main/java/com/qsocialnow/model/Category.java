package com.qsocialnow.model;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private Long id;

    private String descripcion;

    private String nombre;

    private Long idCategoriaOpuesta;

    private List<NameByLanguage> nombrePorIdioma = new ArrayList<NameByLanguage>();

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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getIdCategoriaOpuesta() {
        return idCategoriaOpuesta;
    }

    public void setIdCategoriaOpuesta(Long idCategoriaOpuesta) {
        this.idCategoriaOpuesta = idCategoriaOpuesta;
    }

    public List<NameByLanguage> getNombrePorIdioma() {
        return nombrePorIdioma;
    }

    public void setNombrePorIdioma(List<NameByLanguage> nombrePorIdioma) {
        this.nombrePorIdioma = nombrePorIdioma;
    }

}
