package com.qsocialnow.viewmodel.userresolver;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.qsocialnow.common.model.config.Media;
import com.qsocialnow.common.model.config.UserResolver;

public class UserResolverView {
	
	@Valid
	private UserResolver userResolver;
	
	@NotNull(message = "{field.empty}")
    private Media source;

	public UserResolver getUserResolver() {
		return userResolver;
	}

	public void setUserResolver(UserResolver userResolver) {
		this.userResolver = userResolver;
	}

	public Media getSource() {
		return source;
	}

	public void setSource(Media source) {
		this.source = source;
	}

	
}
