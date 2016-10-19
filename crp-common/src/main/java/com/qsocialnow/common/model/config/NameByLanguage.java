package com.qsocialnow.common.model.config;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qsocialnow.common.util.UserConstants;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NameByLanguage {

    private static final String ES_LANGUAGE = "espaniol";
    private static final String EN_LANGUAGE = "english";
    private static final String PT_LANGUAGE = "portugues";

    private String nombre;

    private Map<String, String> nombrePorIdioma = new HashMap<String, String>();

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Map<String, String> getNombrePorIdioma() {
        return nombrePorIdioma;
    }

    public void setNombrePorIdioma(Map<String, String> nombrePorIdioma) {
        this.nombrePorIdioma = nombrePorIdioma;
    }

    public String getNameByLanguage(String language) {
        String name = null;
        String categoryLanguage = null;
        if (language != null) {
            if (UserConstants.LANGUAGE_SPANISH.equalsIgnoreCase(language)) {
                categoryLanguage = ES_LANGUAGE;
            } else if (UserConstants.LANGUAGE_ENGLISH.equalsIgnoreCase(language)) {
                categoryLanguage = EN_LANGUAGE;
            } else if (UserConstants.LANGUAGE_PORTUGUESE.equalsIgnoreCase(language)) {
                categoryLanguage = PT_LANGUAGE;
            }
            if (categoryLanguage != null) {
                name = nombrePorIdioma.get(categoryLanguage);
            }
        }
        return name != null ? name : this.nombre;

    }

}
