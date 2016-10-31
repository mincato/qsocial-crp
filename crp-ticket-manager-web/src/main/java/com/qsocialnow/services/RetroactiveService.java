package com.qsocialnow.services;

import com.qsocialnow.common.model.retroactive.RetroactiveProcess;
import com.qsocialnow.common.model.retroactive.RetroactiveProcessRequest;

public interface RetroactiveService {

    void executeNewProcess(RetroactiveProcessRequest request);

    RetroactiveProcess getCurrentProcess();

    void cancelProcess();

}
