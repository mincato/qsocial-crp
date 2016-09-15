package com.qsocialnow.model;

import org.hibernate.validator.constraints.NotBlank;

public class RegisterCommentActionView {

    @NotBlank(message = "{field.empty}")
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
