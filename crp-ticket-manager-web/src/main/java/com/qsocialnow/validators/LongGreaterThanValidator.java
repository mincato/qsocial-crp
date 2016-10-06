package com.qsocialnow.validators;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;

public class LongGreaterThanValidator extends AbstractValidator {

	private static final String DEFAULT_KEY_LABEL = "app.long.greater.than.validation";
	private static final String EMPTY_FIELD_KEY_LABEL = "app.field.empty.validation";
	
	public void validate(ValidationContext ctx) {
		Long less = (Long) ctx.getProperty().getValue();
		Long greater = (Long) ctx.getBindContext().getValidatorArg("greater");
		String labelKey = (String) ctx.getBindContext().getValidatorArg("label");
		String componentId = (String) ctx.getBindContext().getValidatorArg("id");
		Boolean required = (Boolean) ctx.getBindContext().getValidatorArg("required");
		if (required != null && required && less == null) {
			addInvalidMessage(ctx, componentId,
					Labels.getLabel(EMPTY_FIELD_KEY_LABEL));
			return;
		}
		if (greater != null && less != null && greater.compareTo(less) > 0) {
			Object[] params = {greater};
			addInvalidMessage(ctx, componentId,
					Labels.getLabel(labelKey != null ? labelKey : DEFAULT_KEY_LABEL , params));
		}
	}
}
