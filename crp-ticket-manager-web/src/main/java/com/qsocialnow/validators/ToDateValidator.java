package com.qsocialnow.validators;

import java.util.Date;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

public class ToDateValidator extends AbstractValidator {

    public void validate(ValidationContext ctx) {
        Date to = (Date) ctx.getProperty().getValue();
        Date from = (Date) ctx.getBindContext().getValidatorArg("fromDate");
        if (from != null && to != null && from.compareTo(to) > 0) {
            addInvalidMessage(ctx, null, "todate");
        }
    }
}
