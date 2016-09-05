package com.qsocialnow.eventresolver.normalizer;

import java.util.Set;

import com.qsocialnow.common.model.event.InPutBeanDocument;
import com.qsocialnow.common.util.StringUtils;

public class NormalizedInputBeanDocument {

    private final InPutBeanDocument inputBeanDocument;

    private String normalizedUsuarioCreacion;

    private String normalizedUsuarioReproduccion;

    private Set<String> normalizedHashTags;

    private Set<String> normalizedActores;

    private Set<String> normalizedTexto;

    public NormalizedInputBeanDocument(InPutBeanDocument inputBeanDocument) {
        this.inputBeanDocument = inputBeanDocument;
    }

    public Long getContinent() {
        return inputBeanDocument.getContinent();
    }

    public Long getCountry() {
        return inputBeanDocument.getCountry();
    }

    public Long getAdm1() {
        return inputBeanDocument.getAdm1();
    }

    public Long getAdm2() {
        return inputBeanDocument.getAdm1();
    }

    public Long getAdm3() {
        return inputBeanDocument.getAdm3();
    }

    public Long getAdm4() {
        return inputBeanDocument.getAdm4();
    }

    public Long getCity() {
        return inputBeanDocument.getCity();
    }

    public Long getNeighborhood() {
        return inputBeanDocument.getNeighborhood();
    }

    public Long[] getCategorias() {
        return inputBeanDocument.getCategorias();
    }

    public Short getConnotacion() {
        return inputBeanDocument.getConnotacion();
    }

    public Long getFollowersCount() {
        return inputBeanDocument.getFollowersCount();
    }

    public String getLanguage() {
        return inputBeanDocument.getLanguage();
    }

    public Long getMedioId() {
        return inputBeanDocument.getMedioId();
    }

    public Long getTimestamp() {
        return inputBeanDocument.getTimestamp();
    }

    public String getTexto() {
        return inputBeanDocument.getTexto();
    }

    public String[] getActores() {
        return inputBeanDocument.getActores();
    }

    public String[] getHashTags() {
        return inputBeanDocument.getHashTags();
    }

    public String getUsuarioCreacion() {
        return inputBeanDocument.getUsuarioCreacion();
    }

    public String getUsuarioReproduccion() {
        return inputBeanDocument.getUsuarioReproduccion();
    }

    public Set<String> getNormalizedHashTags() {
        if (normalizedHashTags == null) {
            normalizedHashTags = StringUtils.convert2LowerCase(getHashTags());
        }
        return normalizedHashTags;
    }

    public Set<String> getNormalizedActores() {
        if (normalizedActores == null) {
            normalizedActores = StringUtils.convert2LowerCase(getActores());
        }
        return normalizedActores;
    }

    public Set<String> getNormalizedTexto(TextNormalizer textNormalizer) {
        if (normalizedTexto == null) {
            normalizedTexto = textNormalizer.normalize(getTexto());
        }
        return normalizedTexto;
    }

}
