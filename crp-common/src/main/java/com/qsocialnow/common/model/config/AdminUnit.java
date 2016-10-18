package com.qsocialnow.common.model.config;

import java.util.List;

public class AdminUnit extends BaseAdminUnit {

    private List<BaseAdminUnit> parents;

    public List<BaseAdminUnit> getParents() {
        return parents;
    }

    public void setParents(List<BaseAdminUnit> parents) {
        this.parents = parents;
    }

}
