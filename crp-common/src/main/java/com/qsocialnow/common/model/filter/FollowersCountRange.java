package com.qsocialnow.common.model.filter;

public class FollowersCountRange {

    private Long gte;

    private Long gt;

    private Long lte;

    private Long lt;

    public Long getGte() {
        return gte;
    }

    public void setGte(Long gte) {
        this.gte = gte;
    }

    public Long getGt() {
        return gt;
    }

    public void setGt(Long gt) {
        this.gt = gt;
    }

    public Long getLte() {
        return lte;
    }

    public void setLte(Long lte) {
        this.lte = lte;
    }

    public Long getLt() {
        return lt;
    }

    public void setLt(Long lt) {
        this.lt = lt;
    }

    public Long getGreaterThan() {
        return gt;
    }

    public Long getLessThan() {
        return lt;
    }

}
