package com.qsocialnow.viewmodel.userresolver;

import java.io.Serializable;
import java.util.HashMap;
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
import com.qsocialnow.services.UserResolverService;

@VariableResolver(DelegatingVariableResolver.class)
@NotifyCommand(value = "modal$closeEvent", onChange = "_vm_.deleted")
@ToClientCommand("modal$closeEvent")
public class DeleteUserResolverViewModel implements Serializable {

    private static final long serialVersionUID = 1021330524834494381L;

    @WireVariable("mockUserResolverService")
    private UserResolverService userResolverService;

    private UserResolverView currentUserResolver;

    private boolean deleted;

    public boolean isDeleted() {
        return deleted;
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
    }

    @Command
    @NotifyChange({ "currentUserResolver", "deleted" })
    public void delete() {
    	userResolverService.delete(currentUserResolver.getUserResolver().getId());
        Clients.showNotification(Labels.getLabel("userresolver.delete.notification.success",
                new String[] { currentUserResolver.getUserResolver().getIdentifier() }));
        deleted = true;
    }

    @Command
    public void close(@ContextParam(ContextType.VIEW) Div comp) {
        comp.detach();
        if (deleted) {
            Map<String, Object> args = new HashMap<String, Object>();
            args.put("userResolverDeleted", currentUserResolver.getUserResolver());
            BindUtils.postGlobalCommand(null, null, "deleteUserResolver", args);
        }
    }

    public String[] getArgs(){
    	if (this.currentUserResolver != null) {
    		return new String[] { this.currentUserResolver.getUserResolver().getIdentifier() };
    	}
    	return null;
    }

}
