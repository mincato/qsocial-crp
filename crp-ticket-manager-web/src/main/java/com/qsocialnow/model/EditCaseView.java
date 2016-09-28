package com.qsocialnow.model;

import java.util.List;

import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.config.BaseUserResolver;
import com.qsocialnow.common.model.config.Segment;
import com.qsocialnow.common.model.config.Trigger;

public class EditCaseView {

    private Case caseObject;

    private Segment segment;

    private Trigger trigger;

    private List<BaseUserResolver> userResolverOptions;

    public void setCaseObject(Case caseObject) {
        this.caseObject = caseObject;
    }

    public Case getCaseObject() {
        return caseObject;
    }

    public Segment getSegment() {
        return segment;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    public List<BaseUserResolver> getUserResolverOptions() {
        return userResolverOptions;
    }

    public void setUserResolverOptions(List<BaseUserResolver> userResolverOptions) {
        this.userResolverOptions = userResolverOptions;
    }
}
