package com.qsocialnow.validators;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;

import com.qsocialnow.viewmodel.team.TeamView;

@Component
public class TeamFormValidator extends AbstractValidator {

    private static final String USERS_FIELD_ID = "users";

    private static final String USERS_LEAST_ONE_VALIDATION_LABEL_KEY = "team.users.least.one.validation";

    private static final String USERS_RESOLVER_FIELD_ID = "usersresolver";

    private static final String USERS_RESOLVER_LEAST_ONE_VALIDATION_LABEL_KEY = "team.usersresolver.least.one.validation";

    @Override
    public void validate(ValidationContext context) {
        Map<String, Property> beanProps = context.getProperties(context.getProperty().getBase());
        TeamView teamView = (TeamView) beanProps.get(".").getValue();
        if (teamView.getUsers().isEmpty()) {
            addInvalidMessage(context, USERS_FIELD_ID, Labels.getLabel(USERS_LEAST_ONE_VALIDATION_LABEL_KEY));
        }
        if (teamView.getUsersResolver().isEmpty()) {
            addInvalidMessage(context, USERS_RESOLVER_FIELD_ID,
                    Labels.getLabel(USERS_RESOLVER_LEAST_ONE_VALIDATION_LABEL_KEY));
        }

    }

}
