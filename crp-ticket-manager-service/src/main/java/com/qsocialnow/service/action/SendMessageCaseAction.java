package com.qsocialnow.service.action;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.ActionRegistry;
import com.qsocialnow.common.model.cases.ActionRegistryStatus;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.Message;
import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.common.services.strategies.SourceMessagePostProcess;
import com.qsocialnow.common.services.strategies.SourceMessageResponse;
import com.qsocialnow.common.services.strategies.SourceStrategy;
import com.qsocialnow.persistence.ActionRegistryRepository;
import com.qsocialnow.persistence.CaseRepository;
import com.qsocialnow.persistence.UserResolverRepository;

@Component("sendMessageCaseAction")
public class SendMessageCaseAction implements AsyncAction, SourceMessagePostProcess {

    @Resource
    private Map<Long, SourceStrategy> sources;

    @Autowired
    private UserResolverRepository userResolverRepository;

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private ActionRegistryRepository actionRegistryRepository;

    @Override
    public AsyncAction execute(Case caseObject, Map<ActionParameter, Object> parameters) {
        String text = (String) parameters.get(ActionParameter.TEXT);
        parameters.put(ActionParameter.COMMENT, text);
        parameters.put(ActionParameter.ACTION_REGISTRY_STATUS, ActionRegistryStatus.SENDING);
        return this;
    }

    @Override
    public void process(SourceMessageResponse response) {
        ActionRegistryStatus status;
        if (response.getError() == null) {
            Case caseObject = caseRepository.findOne(response.getRequest().getCaseId());
            if (caseObject.getIdRootComment() == null)
                caseObject.setIdRootComment(response.getPostId());

            caseObject.addMessage(new Message(response.getPostId(), false));
            caseRepository.update(caseObject);

            status = ActionRegistryStatus.SENT;
        } else {
            status = ActionRegistryStatus.ERROR;
        }
        ActionRegistry actionRegistry = actionRegistryRepository.findOne(response.getRequest().getActionRegistryId());
        actionRegistry.setStatus(status);
        actionRegistryRepository.update(response.getRequest().getCaseId(), actionRegistry);
    }

    @Override
    public void postProcess(Case caseObject, Map<ActionParameter, Object> parameters, ActionRegistry actionRegistry) {
        String text = (String) parameters.get(ActionParameter.TEXT);
        String userResolverId = (String) parameters.get(ActionParameter.USER_RESOLVER);
        UserResolver userResolver = userResolverRepository.findOne(userResolverId);
        sources.get(userResolver.getSource()).sendMessage(caseObject, userResolver, text, actionRegistry.getId());
    }

}
