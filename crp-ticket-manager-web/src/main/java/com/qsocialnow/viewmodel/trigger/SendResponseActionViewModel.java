package com.qsocialnow.viewmodel.trigger;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.config.AutomaticActionCriteria;
import com.qsocialnow.common.model.config.BaseUserResolver;
import com.qsocialnow.common.model.config.Media;
import com.qsocialnow.model.SegmentView;
import com.qsocialnow.model.SendResponseAutomaticActionView;
import com.qsocialnow.model.UserResolverBySource;
import com.qsocialnow.services.TeamService;

@VariableResolver(DelegatingVariableResolver.class)
public class SendResponseActionViewModel implements Serializable {

    private static final long serialVersionUID = -1095251828021707552L;

    @WireVariable
    private TeamService teamService;

    private SendResponseAutomaticActionView sendResponseAction;

    private Integer textMaxlength = 0;

    private boolean anyTwitterSource = false;

    @Init
    public void init(@QueryParam("case") String caseId) {
        sendResponseAction = new SendResponseAutomaticActionView();
    }

    public SendResponseAutomaticActionView getSendResponseAction() {
        return sendResponseAction;
    }

    public void setSendResponseAction(SendResponseAutomaticActionView sendResponseAction) {
        this.sendResponseAction = sendResponseAction;
    }

    @GlobalCommand
    @NotifyChange({ "sendResponseAction", "textMaxlength", "anyTwitterSource" })
    public void show(@BindingParam("segment") SegmentView segment, @BindingParam("action") ActionType action) {
        if (ActionType.REPLY.equals(action) && segment.getTeam() != null) {
            sendResponseAction = new SendResponseAutomaticActionView();
            Map<String, Object> filters = new HashMap<>();
            filters.put("status", true);
            List<BaseUserResolver> userResolvers = teamService.findUserResolvers(segment.getTeam().getId(), filters);
            Map<Media, List<BaseUserResolver>> userResolversByMedia = userResolvers.stream().collect(
                    Collectors.groupingBy(userResolver -> Media.getByValue(userResolver.getSource())));
            List<UserResolverBySource> userResolversBySource = userResolversByMedia.entrySet().stream().map(entry -> {
                UserResolverBySource userResolverBySource = new UserResolverBySource();
                userResolverBySource.setSource(entry.getKey());
                userResolverBySource.setUserResolverOptions(entry.getValue());
                userResolverBySource.setSelectedUserResolver(userResolverBySource.getUserResolverOptions().get(0));
                return userResolverBySource;
            }).collect(Collectors.toList());
            this.sendResponseAction.getUserResolvers().addAll(userResolversBySource);
            if (this.sendResponseAction.getUserResolvers().stream()
                    .anyMatch(userResolver -> Media.TWITTER.equals(userResolver.getSource()))) {
                this.textMaxlength = Media.TWITTER.getMaxlength();
                this.anyTwitterSource = true;
            }
        }
    }

    @Command
    public void save() {
        AutomaticActionCriteria actionCriteria = new AutomaticActionCriteria();
        actionCriteria.setActionType(ActionType.REPLY);
        Map<ActionParameter, Object> parameters = new HashMap<>();
        parameters.put(ActionParameter.TEXT, sendResponseAction.getText());
        List<List<String>> userResolvers = sendResponseAction
                .getUserResolvers()
                .stream()
                .map(userResolver -> {
                    return Arrays.asList(userResolver.getSource().getValue().toString(), userResolver
                            .getSelectedUserResolver().getId());
                }).collect(Collectors.toList());
        parameters.put(ActionParameter.USER_RESOLVERS, userResolvers);
        actionCriteria.setParameters(parameters);

        HashMap<String, Object> args = new HashMap<>();
        args.put("actionCriteria", actionCriteria);
        BindUtils.postGlobalCommand(null, null, "saveActionCriteria", args);
    }

    public Integer getTextMaxlength() {
        return textMaxlength;
    }

    public void setTextMaxlength(Integer textMaxlength) {
        this.textMaxlength = textMaxlength;
    }

    public boolean isAnyTwitterSource() {
        return anyTwitterSource;
    }

    public void setAnyTwitterSource(boolean anyTwitterSource) {
        this.anyTwitterSource = anyTwitterSource;
    }

}
