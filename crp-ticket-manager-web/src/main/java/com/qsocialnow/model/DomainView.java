package com.qsocialnow.model;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.Thematic;

public class DomainView {

    @Valid
    private Domain domain;

    @NotEmpty(message = "{thematics.size}")
    private Set<Thematic> selectedThematics;

    public DomainView() {
        selectedThematics = new HashSet<>();
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public Set<Thematic> getSelectedThematics() {
        return selectedThematics;
    }

    public void setSelectedThematics(Set<Thematic> selectedThematics) {
        this.selectedThematics = selectedThematics;
    }

}
