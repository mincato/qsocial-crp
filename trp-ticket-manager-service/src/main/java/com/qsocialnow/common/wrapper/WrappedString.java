package com.qsocialnow.common.wrapper;

import java.io.Serializable;

public class WrappedString implements Serializable {

    private static final long serialVersionUID = 8256630663958837407L;

    private final String value;

    public WrappedString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
