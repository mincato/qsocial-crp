package com.qsocialnow.validators;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;

import com.qsocialnow.common.model.config.Trigger;

@Component
public class CreateTriggerFormValidator extends AbstractValidator {

	private static final String SEGMENTS_FIELD_ID = "segments";
	
	private static final String SEGMENTS_LEAST_ONE_VALIDATION_LABEL_KEY = "trigger.segments.least.one.validation";
	
	@Override
    public void validate(ValidationContext context) {
        Map<String, Property> beanProps = context.getProperties(context.getProperty().getBase());
        Trigger triggerView = (Trigger) beanProps.get(".").getValue();
        if (triggerView.getSegments().isEmpty()) {
        	addInvalidMessage(context, SEGMENTS_FIELD_ID, Labels.getLabel(SEGMENTS_LEAST_ONE_VALIDATION_LABEL_KEY));
        }
    }

}
