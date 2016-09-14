package com.qsocialnow.viewmodel.userresolver;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.Media;
import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.services.UserResolverService;

@VariableResolver(DelegatingVariableResolver.class)
public class CreateUserResolverViewModel implements Serializable {

	private static final long serialVersionUID = 2083244017777546585L;

	@WireVariable("mockUserResolverService")
	private UserResolverService userResolverService;

	private UserResolverView currentUserResolver;

	private List<Media> mediaTypes;

	@Init
	public void init() {
		initUserResolver();
	}

	private void initUserResolver() {
		currentUserResolver = new UserResolverView();
		currentUserResolver.setUserResolver(new UserResolver());
		currentUserResolver.getUserResolver().setActive(Boolean.FALSE);
		initMedias();
	}

	@Command
	@NotifyChange("currentUserResolver")
	public void save() {
		UserResolver userResolver = new UserResolver();
		userResolver.setSource(currentUserResolver.getSource().getValue());
		userResolver.setIdentifier(currentUserResolver.getUserResolver().getIdentifier());
		userResolver.setActive(currentUserResolver.getUserResolver().getActive());
		userResolver = userResolverService.create(userResolver);
        Clients.showNotification(Labels.getLabel(
				"userresolver.create.notification.success",
				new String[] { userResolver.getIdentifier() }));
		initUserResolver();
	}

	@Command
	@NotifyChange({ "currentUserResolver" })
	public void clear() {
		initUserResolver();
	}

	private void initMedias() {
		mediaTypes = Arrays.asList(Media.values());
	}

	public UserResolverView getCurrentUserResolver() {
		return currentUserResolver;
	}

	public void setCurrentUserResolver(UserResolverView currentUserResolver) {
		this.currentUserResolver = currentUserResolver;
	}

	public List<Media> getMediaTypes() {
		return mediaTypes;
	}

	public void setMediaTypes(List<Media> mediaTypes) {
		this.mediaTypes = mediaTypes;
	}

}