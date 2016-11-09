package com.qsocialnow.viewmodel.userresolver;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.Media;
import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.factories.OauthServiceFactory;
import com.qsocialnow.handler.NotificationHandler;
import com.qsocialnow.services.OauthService;
import com.qsocialnow.services.UserResolverService;

@VariableResolver(DelegatingVariableResolver.class)
public class CreateUserResolverViewModel implements Serializable {

    private static final long serialVersionUID = 2083244017777546585L;

    @WireVariable
    private UserResolverService userResolverService;

    @WireVariable("oauthServiceFactory")
    private OauthServiceFactory oauthServiceFactory;

    private Map<Media, OauthService> oauthServices;

    private UserResolverView currentUserResolver;

    private List<Media> mediaTypes;

    private Boolean authorized;

    @Init
    public void init() {
        initUserResolver();
        initOauthServices();
    }

    @SuppressWarnings("unchecked")
    private void initOauthServices() {
        Object sessionOauthServices = Executions.getCurrent().getSession().getAttribute("oauthServices");
        if (sessionOauthServices == null) {
            oauthServices = new HashMap<>();
            for (Media media : mediaTypes) {
                oauthServices.put(media, oauthServiceFactory.createService(media.getValue()));
            }
        } else {
            oauthServices = (Map<Media, OauthService>) sessionOauthServices;
            oauthServices.get(currentUserResolver.getSource())
                    .resolveCredentials(currentUserResolver.getUserResolver());
            authorized = currentUserResolver.getUserResolver().getCredentials() != null;
            Executions.getCurrent().getSession().removeAttribute("oauthServices");
        }
    }

    private void initUserResolver() {
        Object sessionUserResolver = Executions.getCurrent().getSession().getAttribute("currentUserResolver");
        if (sessionUserResolver == null) {
            currentUserResolver = new UserResolverView();
            currentUserResolver.setUserResolver(new UserResolver());
            currentUserResolver.getUserResolver().setActive(Boolean.FALSE);
            authorized = null;
        } else {
            currentUserResolver = (UserResolverView) sessionUserResolver;
            Executions.getCurrent().getSession().removeAttribute("currentUserResolver");
        }
        initMedias();
    }

    @Command
    @NotifyChange({ "authorized", "currentUserResolver" })
    public void save() {
        if (Boolean.TRUE.equals(authorized)) {
            UserResolver userResolver = new UserResolver();

            userResolver.setSource(currentUserResolver.getSource().getValue());
            userResolver.setIdentifier(currentUserResolver.getUserResolver().getIdentifier());
            userResolver.setActive(currentUserResolver.getUserResolver().isActive());
            userResolver.setCredentials(currentUserResolver.getUserResolver().getCredentials());
            userResolver.setSourceId(currentUserResolver.getUserResolver().getSourceId());
            userResolver = userResolverService.create(userResolver);
            NotificationHandler.addNotification(Labels.getLabel("userresolver.create.notification.success",
                    new String[] { userResolver.getIdentifier() }));
            initUserResolver();
            Executions.getCurrent().sendRedirect("/pages/user-resolver/list/index.zul");
        } else {
            authorized = false;
        }
    }

    @Command
    @NotifyChange({ "currentUserResolver", "authorized" })
    public void clear() {
        initUserResolver();
    }

    @Command
    public void authorize(@BindingParam("media") Media media) {
        String url = oauthServices.get(media).getAuthorizationUrl();
        Executions.getCurrent().getSession().setAttribute("currentUserResolver", currentUserResolver);
        Executions.getCurrent().getSession().setAttribute("oauthServices", oauthServices);
        Executions.getCurrent().sendRedirect(url);
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

    public Boolean getAuthorized() {
        return authorized;
    }

}