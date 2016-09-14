package com.qsocialnow.viewmodel.userresolver;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.NotifyCommand;
import org.zkoss.bind.annotation.ToClientCommand;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Div;

import com.qsocialnow.common.model.config.Media;
import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.services.UserResolverService;

@VariableResolver(DelegatingVariableResolver.class)
@NotifyCommand(value = "modal$closeEvent", onChange = "_vm_.saved")
@ToClientCommand("modal$closeEvent")
public class EditUserResolverViewModel implements Serializable {

    private static final long serialVersionUID = 1021330524834494381L;

    @WireVariable("mockUserResolverService")
    private UserResolverService userResolverService;

    private UserResolverView currentUserResolver;

    private List<Media> mediaTypes;

    private boolean saved;

    public boolean isSaved() {
        return saved;
    }

    public UserResolverView getCurrentUserResolver() {
        return currentUserResolver;
    }

    public void setCurrentUserResolver(UserResolverView currentUserResolver) {
        this.currentUserResolver = currentUserResolver;
    }

    @Init
    public void init(@BindingParam("userresolver") String userResolverId) {
        currentUserResolver = new UserResolverView();
        currentUserResolver.setUserResolver(userResolverService.findOne(userResolverId));
        currentUserResolver.setSource(Media.getByValue(currentUserResolver.getUserResolver().getSource()));
        initMedias();
    }

    @Command
    @NotifyChange({ "currentUserResolver", "saved" })
    public void save() {
        UserResolver userResolver = currentUserResolver.getUserResolver();
        userResolver.setSource(currentUserResolver.getSource().getValue());
        currentUserResolver.setUserResolver(userResolverService.update(userResolver));
        Clients.showNotification(Labels.getLabel("userresolver.edit.notification.success",
                new String[] { currentUserResolver.getUserResolver().getIdentifier() }));
        saved = true;
    }

    @Command
    public void close(@ContextParam(ContextType.VIEW) Div comp) {
        comp.detach();
        if (saved) {
            Map<String, Object> args = new HashMap<String, Object>();
            args.put("userResolverChanged", currentUserResolver.getUserResolver());
            BindUtils.postGlobalCommand(null, null, "changeUserResolver", args);
        }
    }

    private void initMedias() {
        mediaTypes = Arrays.asList(Media.values());
    }

    public List<Media> getMediaTypes() {
        return mediaTypes;
    }

    public void setMediaTypes(List<Media> mediaTypes) {
        this.mediaTypes = mediaTypes;
    }

}
