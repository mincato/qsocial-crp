package com.qsocialnow.validators;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;

@Component
public class DateValidator extends AbstractValidator {

    @Override
    public void validate(ValidationContext context) {
        Date mainDate = (Date) context.getProperty().getValue();
        Boolean required = (Boolean) context.getBindContext().getValidatorArg("required");
        String key = (String) context.getBindContext().getValidatorArg("key");
        if (required) {
            if (mainDate == null) {
                addInvalidMessage(context, key, Labels.getLabel("field.empty"));
            } else {
                Object startDateProperty = context.getBindContext().getValidatorArg("startDate");
                Object endDateProperty = context.getBindContext().getValidatorArg("endDate");

                Date startDate = null;
                Date endDate = null;
                if (startDateProperty != null) {
                    Property[] startDateObject = context.getProperties("init");
                    if (startDateObject != null) {
                        startDate = (Date) startDateObject[0].getValue();
                    }
                    endDate = mainDate;
                } else if (endDateProperty != null) {
                    startDate = mainDate;
                    Property[] endDateObject = context.getProperties("end");
                    if (endDateObject != null) {
                        endDate = (Date) endDateObject[0].getValue();
                    }
                }

                if (startDate != null && endDate != null) {
                    if (!startDate.before(endDate)) {
                        String label;
                        if (mainDate == startDate) {
                            label = "date.should.before";
                        } else {
                            label = "date.should.after";
                        }
                        addInvalidMessage(context, key, Labels.getLabel(label));
                    }
                }
            }
        }
    }

}
