package com.qsocialnow.rest.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class BackEndError implements Serializable {

    private static final long serialVersionUID = 5908831902887077484L;

    private static final Integer DEFAULT_HTTP_CODE = 500;

    @JsonIgnore
    private Integer httpCode;

    private Long code;
    private String message;

    private Object data;

    public BackEndError() {
    }

    public BackEndError(Long code, String message) {
        this.code = code;
        this.message = message;
    }

    public BackEndError(BackEndError error) {
        this.httpCode = error.getHttpCode();
        this.code = error.getCode();
        this.message = error.getMessage();
        this.data = error.getData();
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setHttpCode(Integer httpCode) {
        this.httpCode = httpCode;
    }

    public Integer getHttpCode() {
        if (httpCode == null) {
            httpCode = DEFAULT_HTTP_CODE;
        }
        return httpCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
