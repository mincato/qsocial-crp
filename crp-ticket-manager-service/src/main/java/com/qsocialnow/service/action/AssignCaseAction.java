package com.qsocialnow.service.action;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.config.BaseUserResolver;
import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.persistence.UserResolverRepository;

@Component("assignCaseAction")
public class AssignCaseAction implements Action {

    @Autowired
    private UserResolverRepository userResolverRepository;

    @Override
    public boolean execute(Case caseObject, Map<ActionParameter, Object> parameters) {
        String userResolverId = (String) parameters.get(ActionParameter.USER_RESOLVER);
        UserResolver userResolver = userResolverRepository.findOne(userResolverId);
        BaseUserResolver oldUserResolver = caseObject.getUserResolver();
        caseObject.setUserResolver(new BaseUserResolver(userResolver));
        parameters.put(ActionParameter.COMMENT, buildComment(oldUserResolver, userResolver));
        return true;
    }

    private String buildComment(BaseUserResolver oldUserResolver, UserResolver userResolver) {
        StringBuilder sb = new StringBuilder();
        if (oldUserResolver != null) {
            sb.append(oldUserResolver.getIdentifier());
            sb.append(" --> ");
        }
        sb.append(userResolver.getIdentifier());
        return sb.toString();
    }

}
