package com.qsocialnow.common.services.strategies;

import java.util.List;

public interface UserToTrackTask<RQ> {

    void addNewTrackFilters(List<RQ> tracks);
}
