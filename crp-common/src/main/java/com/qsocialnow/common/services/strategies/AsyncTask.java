package com.qsocialnow.common.services.strategies;

public interface AsyncTask<RQ, RS> {

    RS execute(RQ request);

}
