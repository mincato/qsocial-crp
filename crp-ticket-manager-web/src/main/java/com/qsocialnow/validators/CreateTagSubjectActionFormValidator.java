package com.qsocialnow.validators;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;

import com.qsocialnow.model.TagSubjectActionView;

@Component
public class CreateTagSubjectActionFormValidator extends AbstractValidator {

    private static final String CATEGORIES_FIELD_ID = "categories";

    private static final String CATEGORIES_LEAST_ONE_VALIDATION_LABEL_KEY = "trigger.action.tagSubject.least.one.validation";

    @Override
    public void validate(ValidationContext context) {
        Map<String, Property> beanProps = context.getProperties(context.getProperty().getBase());
        TagSubjectActionView tagSubjectActionView = (TagSubjectActionView) beanProps.get(".").getValue();
        if (tagSubjectActionView.getCategorySets().isEmpty()) {
            addInvalidMessage(context, CATEGORIES_FIELD_ID, Labels.getLabel(CATEGORIES_LEAST_ONE_VALIDATION_LABEL_KEY));
        }
    }

}
