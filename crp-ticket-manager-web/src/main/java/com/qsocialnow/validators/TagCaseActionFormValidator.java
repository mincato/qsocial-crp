package com.qsocialnow.validators;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;

import com.qsocialnow.model.TagCaseActionView;

@Component
public class TagCaseActionFormValidator extends AbstractValidator {

    private static final String CATEGORIES_FIELD_ID = "categorySets";

    private static final String CATEGORIES_LEAST_ONE_VALIDATION_LABEL_KEY = "cases.actions.tag.categories.least.one.validation";

    @Override
    public void validate(ValidationContext context) {
        Map<String, Property> beanProps = context.getProperties(context.getProperty().getBase());
        TagCaseActionView tagCaseActionView = (TagCaseActionView) beanProps.get(".").getValue();
        if (tagCaseActionView.getCategorySets().isEmpty()) {
            addInvalidMessage(context, CATEGORIES_FIELD_ID, Labels.getLabel(CATEGORIES_LEAST_ONE_VALIDATION_LABEL_KEY));
            return;
        }
    }

}
