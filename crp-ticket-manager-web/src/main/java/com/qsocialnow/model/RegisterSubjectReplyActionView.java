package com.qsocialnow.model;

import org.hibernate.validator.constraints.NotBlank;

public class RegisterSubjectReplyActionView {

    @NotBlank(message = "app.field.empty.validation")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
