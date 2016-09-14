package com.qsocialnow.common.model.config;

import java.io.Serializable;

public class UserResolverListView implements Serializable {

	private static final long serialVersionUID = -4642800987682580389L;

	private String id;

	private Long source;

	private String identifier;

	private Boolean active;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getSource() {
		return source;
	}

	public void setSource(Long source) {
		this.source = source;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	public Media getMedia(){
		return Media.getByValue(this.source);
	}

}