package com.qsocialnow.common.model.retroactive;

import com.qsocialnow.common.model.config.AdminUnit;

public class AdministrativeUnitsFilterBean {

    private AdminUnit adminUnit;

    private Long continent;

    private Long country;

    private Long city;

    private Long neighborhood;

    private Long adm1;

    private Long adm2;

    private Long adm3;

    private Long adm4;

    public Long getContinent() {
        return continent;
    }

    public void setContinent(Long continent) {
        this.continent = continent;
    }

    public Long getCountry() {
        return country;
    }

    public void setCountry(Long country) {
        this.country = country;
    }

    public Long getCity() {
        return city;
    }

    public void setCity(Long city) {
        this.city = city;
    }

    public Long getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(Long neighborhood) {
        this.neighborhood = neighborhood;
    }

    public Long getAdm1() {
        return adm1;
    }

    public void setAdm1(Long adm1) {
        this.adm1 = adm1;
    }

    public Long getAdm2() {
        return adm2;
    }

    public void setAdm2(Long adm2) {
        this.adm2 = adm2;
    }

    public Long getAdm3() {
        return adm3;
    }

    public void setAdm3(Long adm3) {
        this.adm3 = adm3;
    }

    public Long getAdm4() {
        return adm4;
    }

    public void setAdm4(Long adm4) {
        this.adm4 = adm4;
    }

    public AdminUnit getAdminUnit() {
        return adminUnit;
    }

    public void setAdminUnit(AdminUnit adminUnit) {
        this.adminUnit = adminUnit;
    }

}
