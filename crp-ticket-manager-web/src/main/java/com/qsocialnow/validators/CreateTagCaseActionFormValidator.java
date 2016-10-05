package com.qsocialnow.validators;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;

import com.qsocialnow.model.TagCaseActionView;

@Component
public class CreateTagCaseActionFormValidator extends AbstractValidator {

    private static final String CATEGORIES_FIELD_ID = "categories";

    private static final String CRITERIAS_LEAST_ONE_VALIDATION_LABEL_KEY = "trigger.action.tagCase.least.one.validation";

    @Override
    public void validate(ValidationContext context) {
        Map<String, Property> beanProps = context.getProperties(context.getProperty().getBase());
        TagCaseActionView tagCaseActionView = (TagCaseActionView) beanProps.get(".").getValue();
        if (tagCaseActionView.getCategorySets().isEmpty()) {
            addInvalidMessage(context, CATEGORIES_FIELD_ID, Labels.getLabel(CRITERIAS_LEAST_ONE_VALIDATION_LABEL_KEY));
        }
    }

}
