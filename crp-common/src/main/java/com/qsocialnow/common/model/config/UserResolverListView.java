package com.qsocialnow.common.model.config;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class UserResolverListView implements Serializable {

    private static final long serialVersionUID = -4642800987682580389L;

    private String id;

    private Long source;

    private String identifier;

    private boolean active;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getSource() {
        return source;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Media getMedia() {
        return Media.getByValue(this.source);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        UserResolverListView urlv = (UserResolverListView) obj;
        return new EqualsBuilder().appendSuper(super.equals(obj)).append(id, urlv.id).isEquals();
    }

}