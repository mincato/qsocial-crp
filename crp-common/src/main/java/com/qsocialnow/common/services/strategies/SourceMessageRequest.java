package com.qsocialnow.common.services.strategies;

import java.io.Serializable;

import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.config.UserResolver;

public class SourceMessageRequest implements Serializable {

    private static final long serialVersionUID = -7620204744787684926L;

    private String caseId;

    private String subjectIdentifier;

    private UserResolver userResolver;

    private String text;

    private String postId;

    private ActionType action;

    private String actionRegistryId;

    private String idOriginal;

    public String getSubjectIdentifier() {
        return subjectIdentifier;
    }

    public void setSubjectIdentifier(String subjectIdentifier) {
        this.subjectIdentifier = subjectIdentifier;
    }

    public UserResolver getUserResolver() {
        return userResolver;
    }

    public void setUserResolver(UserResolver userResolver) {
        this.userResolver = userResolver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public ActionType getAction() {
        return action;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public void setActionRegistryId(String actionRegistryId) {
        this.actionRegistryId = actionRegistryId;
    }

    public String getActionRegistryId() {
        return actionRegistryId;
    }

    public String getIdOriginal() {
        return idOriginal;
    }

    public void setIdOriginal(String idOriginal) {
        this.idOriginal = idOriginal;
    }
}
