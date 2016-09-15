package com.qsocialnow.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.qsocialnow.common.model.config.UserResolver;

public class SendResponseActionView {

    @NotNull(message = "{userResolver.null}")
    private UserResolver selectedUserResolver;

    @NotBlank(message = "{field.empty}")
    private String text;

    public UserResolver getSelectedUserResolver() {
        return selectedUserResolver;
    }

    public void setSelectedUserResolver(UserResolver selectedUserResolver) {
        this.selectedUserResolver = selectedUserResolver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
