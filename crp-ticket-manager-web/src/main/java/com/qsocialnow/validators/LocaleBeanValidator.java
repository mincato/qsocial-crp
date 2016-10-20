package com.qsocialnow.validators;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.BeanValidator;
import org.zkoss.util.resource.Labels;

public class LocaleBeanValidator extends BeanValidator {

	protected void addInvalidMessage(ValidationContext ctx,String message) {
		super.addInvalidMessage(ctx, Labels.getLabel(message));
	}
}
