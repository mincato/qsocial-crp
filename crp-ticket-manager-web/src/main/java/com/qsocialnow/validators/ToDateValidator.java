package com.qsocialnow.validators;

import java.util.Date;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;

public class ToDateValidator extends AbstractValidator {

    private static final String DEFAULT_KEY_LABEL = "app.todate.validation";
    private static final String EMPTY_FIELD_KEY_LABEL = "app.field.empty.validation";

    public void validate(ValidationContext ctx) {
        Date to = (Date) ctx.getProperty().getValue();
        Date from = (Date) ctx.getBindContext().getValidatorArg("fromDate");
        String labelKey = (String) ctx.getBindContext().getValidatorArg("label");
        String componentId = (String) ctx.getBindContext().getValidatorArg("id");
        Boolean required = (Boolean) ctx.getBindContext().getValidatorArg("required");
        if (required != null && required && to == null) {
            addInvalidMessage(ctx, componentId, Labels.getLabel(EMPTY_FIELD_KEY_LABEL));
            return;
        }
        if (from != null && to != null && from.compareTo(to) > 0) {
            addInvalidMessage(ctx, componentId, Labels.getLabel(labelKey != null ? labelKey : DEFAULT_KEY_LABEL));
        }
    }
}
