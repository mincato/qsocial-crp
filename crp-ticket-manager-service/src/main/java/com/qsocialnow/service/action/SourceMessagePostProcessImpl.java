package com.qsocialnow.service.action;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.services.strategies.SourceMessagePostProcess;
import com.qsocialnow.common.services.strategies.SourceMessageResponse;

@Component
public class SourceMessagePostProcessImpl implements SourceMessagePostProcess {

    @Resource
    private Map<ActionType, SourceMessagePostProcess> postProcessActions;

    @Override
    public void process(SourceMessageResponse response) {
        postProcessActions.get(response.getRequest().getAction()).process(response);
    }

}
