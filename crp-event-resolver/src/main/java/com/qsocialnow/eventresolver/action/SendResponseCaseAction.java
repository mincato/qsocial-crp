package com.qsocialnow.eventresolver.action;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.Message;
import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.common.services.strategies.SourceStrategy;
import com.qsocialnow.elasticsearch.services.config.UserResolverService;
import com.qsocialnow.eventresolver.processor.ExecutionMessageRequest;

@Component("sendResponseCaseAction")
public class SendResponseCaseAction implements Action {

    private static Logger log = LoggerFactory.getLogger(SendResponseCaseAction.class);

    @Resource
    private Map<Long, SourceStrategy> sources;

    @Autowired
    private UserResolverService userResolverService;

    @Override
    @SuppressWarnings("unchecked")
    public Case execute(Case caseObject, Map<ActionParameter, Object> parameters, ExecutionMessageRequest request) {
        if (caseObject.getSource() != null) {
            String text = (String) parameters.get(ActionParameter.TEXT);
            List<List<String>> userResolvers = (List<List<String>>) parameters.get(ActionParameter.USER_RESOLVERS);
            Optional<String> userResolverIdOptional = userResolvers.stream()
                    .filter(userResolver -> caseObject.getSource().equals(Long.parseLong(userResolver.get(0))))
                    .map(tuple -> tuple.get(1)).findFirst();
            if (userResolverIdOptional.isPresent()) {
                UserResolver userResolver = userResolverService.findOne(userResolverIdOptional.get());
                String postId = sources.get(caseObject.getSource()).sendResponse(caseObject, userResolver, text);
                caseObject.addMessage(new Message(postId, false));
                parameters.put(ActionParameter.COMMENT, postId + " - " + text);
            } else {
                log.warn("There is no user resolver defined to send message for source: " + caseObject.getSource());
            }
        } else {
            log.warn("The source is not set on the case object");
        }
        return caseObject;
    }

}
