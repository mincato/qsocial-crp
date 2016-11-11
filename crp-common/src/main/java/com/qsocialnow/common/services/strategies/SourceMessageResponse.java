package com.qsocialnow.common.services.strategies;

import com.qsocialnow.common.model.cases.ErrorType;

public class SourceMessageResponse {

    private SourceMessageRequest request;

    private String postId;

    private ErrorType errorType;

    private String sourceErrorMessage;

    public SourceMessageRequest getRequest() {
        return request;
    }

    public void setRequest(SourceMessageRequest request) {
        this.request = request;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }

    public void setSourceErrorMessage(String sourceErrorMessage) {
        this.sourceErrorMessage = sourceErrorMessage;
    }

    public String getSourceErrorMessage() {
        return sourceErrorMessage;
    }
}
