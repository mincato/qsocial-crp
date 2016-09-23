package com.qsocialnow.validators;

import java.text.MessageFormat;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;

import com.qsocialnow.model.CaseView;

@Component
public class CreateCaseFormValidator extends AbstractValidator {

    private static final String FIELD_EMPTY_LABEL_KEY = "domain.field.empty";

    private static final String RESOLUTIONS_FIELD_ID = "resolutions";

    @Override
    public void validate(ValidationContext context) {
        /*
         * Map<String, Property> beanProps =
         * context.getProperties(context.getProperty().getBase()); CaseView
         * caseView = (CaseView) beanProps.get(".").getValue();
         * 
         * if (StringUtils.isBlank(caseView.getNewCase().getTitle())){
         * addInvalidMessage(context,RESOLUTIONS_FIELD_ID,
         * Labels.getLabel(FIELD_EMPTY_LABEL_KEY)); return; }
         */

    }

}
