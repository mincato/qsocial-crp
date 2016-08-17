package com.qsocialnow.common.exception;

import java.util.ArrayList;
import java.util.List;

public class BackEndException extends RuntimeException {

    private static final long serialVersionUID = -1418765195725961556L;

    private List<Object> params;

    private Object data;

    public BackEndException() {
        super();
    }

    public BackEndException(Throwable t) {
        super(t);
    }

    public BackEndException(String message) {
        super(message);
    }

    public BackEndException(String message, Throwable t) {
        super(message, t);
    }

    public void addParam(Object param) {
        if (params == null) {
            params = new ArrayList<>();
        }
        params.add(param);
    }

    public List<Object> getParams() {
        return params;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

}
