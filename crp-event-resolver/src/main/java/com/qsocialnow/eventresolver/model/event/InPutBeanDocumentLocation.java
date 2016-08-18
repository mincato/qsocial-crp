package com.qsocialnow.eventresolver.model.event;

import java.io.Serializable;

public class InPutBeanDocumentLocation implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8875193134922331125L;
    private Double lat;
    private Double lon;

    public InPutBeanDocumentLocation() {
    }

    public Double getLatitud() {
        return lat;
    }

    public void setLatitud(Double latitud) {
        this.lat = latitud;
    }

    public Double getLongitud() {
        return lon;
    }

    public void setLongitud(Double longitud) {
        this.lon = longitud;
    }

}
