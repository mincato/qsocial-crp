package com.qsocialnow.model;

import org.hibernate.validator.constraints.NotBlank;

public class RegisterCommentActionView {

    @NotBlank(message = "app.field.empty.validation")
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
