package com.qsocialnow.rest.security;

import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority {

    private static final long serialVersionUID = 1510757465998698134L;

    private String authority;

    public Authority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

}
