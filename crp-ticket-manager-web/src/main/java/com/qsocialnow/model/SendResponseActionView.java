package com.qsocialnow.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.qsocialnow.common.model.config.BaseUserResolver;

public class SendResponseActionView {

    @NotNull(message = "{userResolver.null}")
    private BaseUserResolver selectedUserResolver;

    @NotBlank(message = "{field.empty}")
    private String text;

    public BaseUserResolver getSelectedUserResolver() {
        return selectedUserResolver;
    }

    public void setSelectedUserResolver(BaseUserResolver selectedUserResolver) {
        this.selectedUserResolver = selectedUserResolver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
