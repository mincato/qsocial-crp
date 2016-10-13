package com.qsocialnow.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private Integer idUsuario;

    private String nombreDeLogin;

    private Boolean esOdatech;

    private Boolean esAdminDePrc;

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreDeLogin() {
        return nombreDeLogin;
    }

    public void setNombreDeLogin(String nombreDeLogin) {
        this.nombreDeLogin = nombreDeLogin;
    }

    public Boolean getEsOdatech() {
        return esOdatech;
    }

    public void setEsOdatech(Boolean esOdatech) {
        this.esOdatech = esOdatech;
    }

    public Boolean getEsAdminDePrc() {
        return esAdminDePrc;
    }

    public void setEsAdminDePrc(Boolean esAdminDePrc) {
        this.esAdminDePrc = esAdminDePrc;
    }

}
