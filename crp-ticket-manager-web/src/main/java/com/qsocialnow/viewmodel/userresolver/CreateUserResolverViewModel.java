package com.qsocialnow.viewmodel.userresolver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.model.Media;
import com.qsocialnow.model.MediaView;
import com.qsocialnow.services.UserResolverService;

@VariableResolver(DelegatingVariableResolver.class)
public class CreateUserResolverViewModel implements Serializable {

	private static final long serialVersionUID = 2083244017777546585L;

	@WireVariable("mockUserResolverService")
	private UserResolverService userResolverService;

	private UserResolver currentUserResolver;

	private List<MediaView> mediaTypes;

	@Init
	public void init() {
		initUserResolver();
	}

	private void initUserResolver() {
		currentUserResolver = new UserResolver();
		currentUserResolver.setActive(Boolean.FALSE);
		initMedias();
	}

	@Command
	@NotifyChange("currentUserResolver")
	public void save() {
		UserResolver userResolver = userResolverService.create(currentUserResolver);
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
		mediaTypes = new ArrayList<>();
		for (Media media : Media.values()) {
			MediaView mediaView = new MediaView();
			mediaView.setMedia(media);
			mediaView.setChecked(false);
			mediaTypes.add(mediaView);
		}
	}

	public UserResolver getCurrentUserResolver() {
		return currentUserResolver;
	}

	public void setCurrentUserResolver(UserResolver currentUserResolver) {
		this.currentUserResolver = currentUserResolver;
	}

	public List<MediaView> getMediaTypes() {
		return mediaTypes;
	}

	public void setMediaTypes(List<MediaView> mediaTypes) {
		this.mediaTypes = mediaTypes;
	}

}