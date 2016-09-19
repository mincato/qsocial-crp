package com.qsocialnow.viewmodel;

import java.io.Serializable;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.model.ApplicationVersion;
import com.qsocialnow.services.ApplicationService;

@VariableResolver(DelegatingVariableResolver.class)
public class HomeViewModel implements Serializable {

	private static final long serialVersionUID = 3419130654069105310L;

	@WireVariable
	private ApplicationService applicationService;

	private ApplicationVersion applicationVersion;

	@Init
	public void init() {
		applicationVersion = applicationService.getVersion();
	}

	public ApplicationVersion getApplicationVersion() {
		return applicationVersion;
	}

	public void setApplicationVersion(ApplicationVersion applicationVersion) {
		this.applicationVersion = applicationVersion;
	}

}
