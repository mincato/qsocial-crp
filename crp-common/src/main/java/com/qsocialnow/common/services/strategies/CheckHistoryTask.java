package com.qsocialnow.common.services.strategies;

public interface CheckHistoryTask<RQ> {

    void checkHistory(RQ event);

}
