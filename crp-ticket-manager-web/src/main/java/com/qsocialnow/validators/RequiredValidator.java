package com.qsocialnow.validators;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;

public class RequiredValidator extends AbstractValidator {

    private static final String EMPTY_FIELD_KEY_LABEL = "app.field.empty.validation";

    public void validate(ValidationContext ctx) {
        Object object = ctx.getProperty().getValue();
        String componentId = (String) ctx.getBindContext().getValidatorArg("id");
        if (object == null) {
            addInvalidMessage(ctx, componentId, Labels.getLabel(EMPTY_FIELD_KEY_LABEL));
            return;
        }
    }
}
