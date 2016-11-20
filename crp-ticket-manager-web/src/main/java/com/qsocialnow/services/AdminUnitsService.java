package com.qsocialnow.services;

import java.util.List;

import com.qsocialnow.common.model.config.AdminUnit;

public interface AdminUnitsService {

    public List<AdminUnit> findUnitAdminsByGeoIds(String query, Object parameters);
}
