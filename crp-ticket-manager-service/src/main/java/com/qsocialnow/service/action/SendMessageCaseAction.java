package com.qsocialnow.service.action;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.persistence.UserResolverRepository;
import com.qsocialnow.service.strategies.SourceStrategy;

@Component("sendMessageCaseAction")
public class SendMessageCaseAction implements Action {

    @Resource
    private Map<Long, SourceStrategy> sources;

    @Autowired
    private UserResolverRepository userResolverRepository;

    @Override
    public boolean execute(Case caseObject, Map<ActionParameter, Object> parameters) {
        String text = (String) parameters.get(ActionParameter.TEXT);
        String userResolverId = (String) parameters.get(ActionParameter.USER_RESOLVER);
        UserResolver userResolver = userResolverRepository.findOne(userResolverId);
        String postId = sources.get(userResolver.getSource()).sendMessage(caseObject, userResolver, text);
        parameters.put(ActionParameter.COMMENT, postId + " - " + text);
        return false;
    }

}
