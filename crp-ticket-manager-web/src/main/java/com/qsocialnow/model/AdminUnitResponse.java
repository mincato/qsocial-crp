package com.qsocialnow.model;

import java.util.List;

import com.qsocialnow.common.model.config.AdminUnit;

public class AdminUnitResponse {

    private List<AdminUnit> administrativeUnits;

    public List<AdminUnit> getAdministrativeUnits() {
        return administrativeUnits;
    }

    public void setAdministrativeUnits(List<AdminUnit> administrativeUnits) {
        this.administrativeUnits = administrativeUnits;
    }

}
