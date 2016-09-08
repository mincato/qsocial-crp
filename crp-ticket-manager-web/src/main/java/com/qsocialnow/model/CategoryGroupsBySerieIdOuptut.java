package com.qsocialnow.model;

import java.util.ArrayList;
import java.util.List;

public class CategoryGroupsBySerieIdOuptut {

    private Long id;

    private List<CategoryGroup> conjuntos = new ArrayList<CategoryGroup>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CategoryGroup> getConjuntos() {
        return conjuntos;
    }

    public void setConjuntos(List<CategoryGroup> conjuntos) {
        this.conjuntos = conjuntos;
    }

}
