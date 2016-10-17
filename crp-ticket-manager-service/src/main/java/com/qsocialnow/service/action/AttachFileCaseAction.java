package com.qsocialnow.service.action;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.Case;

@Component("attachFileCaseAction")
public class AttachFileCaseAction implements Action {

    @SuppressWarnings("unchecked")
    @Override
    public void execute(Case caseObject, Map<ActionParameter, Object> parameters) {
        List<String> files = (List<String>) parameters.get(ActionParameter.FILES);
        caseObject.setAttachments(new HashSet<>(files));
        parameters.put(ActionParameter.COMMENT, StringUtils.join(files, ", "));
    }

}
