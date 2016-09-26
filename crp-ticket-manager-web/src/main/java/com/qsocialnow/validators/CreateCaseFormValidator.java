package com.qsocialnow.validators;

import org.springframework.stereotype.Component;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

@Component
public class CreateCaseFormValidator extends AbstractValidator {

    // private static final String FIELD_EMPTY_LABEL_KEY = "cases.field.empty";

    // private static final String TITLE_FIELD_ID = "title";

    @Override
    public void validate(ValidationContext context) {
        /*
         * Map<String, Property> beanProps =
         * context.getProperties(context.getProperty().getBase()); CaseView
         * caseView = (CaseView) beanProps.get(".").getValue();
         * 
         * if (StringUtils.isBlank(caseView.getNewCase().getTitle())) {
         * addInvalidMessage(context, TITLE_FIELD_ID,
         * Labels.getLabel(FIELD_EMPTY_LABEL_KEY)); return; }
         */

    }

}
