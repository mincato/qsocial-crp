package com.qsocialnow.viewmodel;

public enum CasesReportOption {

    ADMIN(false, true, false, false), MAP(false, false, false, true), RESOLUTION(true, false, false, false), STATE(
            false, false, true, false);

    private boolean byResolution;

    private boolean byAdmin;

    private boolean byState;

    private boolean byMap;

    private CasesReportOption(boolean byResolution, boolean byAdmin, boolean byState, boolean byMap) {
        this.byResolution = byResolution;
        this.byAdmin = byAdmin;
        this.byState = byState;
        this.byMap = byMap;
    }

    public boolean isByResolution() {
        return byResolution;
    }

    public void setByResolution(boolean byResolution) {
        this.byResolution = byResolution;
    }

    public boolean isByAdmin() {
        return byAdmin;
    }

    public void setByAdmin(boolean byAdmin) {
        this.byAdmin = byAdmin;
    }

    public boolean isByState() {
        return byState;
    }

    public void setByState(boolean byState) {
        this.byState = byState;
    }

    public boolean isByMap() {
        return byMap;
    }

    public void setByMap(boolean byMap) {
        this.byMap = byMap;
    }

}
