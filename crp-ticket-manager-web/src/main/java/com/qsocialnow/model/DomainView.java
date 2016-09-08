package com.qsocialnow.model;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.common.model.config.Thematic;

public class DomainView {

    @Valid
    private Domain domain;

    @NotEmpty(message = "{thematics.size}")
    private Set<Thematic> selectedThematics;

    private List<Resolution> resolutions;
    
    public DomainView() {
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

	public List<Resolution> getResolutions() {
		return resolutions;
	}

	public void setResolutions(List<Resolution> resolutions) {
		this.resolutions = resolutions;
	}
}
