package com.qsocialnow.common.services.strategies;

public class SourceMessageResponse {

    private SourceMessageRequest request;

    private String postId;

    private Exception error;

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

    public void setError(Exception error) {
        this.error = error;
    }

    public Exception getError() {
        return error;
    }

}
