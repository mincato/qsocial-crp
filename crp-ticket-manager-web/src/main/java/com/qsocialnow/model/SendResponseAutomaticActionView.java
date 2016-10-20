package com.qsocialnow.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

public class SendResponseAutomaticActionView {

    private List<UserResolverBySource> userResolvers;

    @NotBlank(message = "app.field.empty.validation")
    private String text;

    public SendResponseAutomaticActionView() {
        userResolvers = new ArrayList<>();
    }

    public List<UserResolverBySource> getUserResolvers() {
        return userResolvers;
    }

    public void setUserResolvers(List<UserResolverBySource> userResolvers) {
        this.userResolvers = userResolvers;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
