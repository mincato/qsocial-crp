package com.qsocialnow.validators;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;

import com.qsocialnow.model.SegmentView;

@Component
public class CreateSegmentFormValidator extends AbstractValidator {

    private static final String CRITERIAS_FIELD_ID = "criterias";

    private static final String CRITERIAS_LEAST_ONE_VALIDATION_LABEL_KEY = "trigger.criterias.least.one.validation";

    @Override
    public void validate(ValidationContext context) {
        Map<String, Property> beanProps = context.getProperties(context.getProperty().getBase());
        SegmentView segmentView = (SegmentView) beanProps.get(".").getValue();
        if (segmentView.getSegment().getDetectionCriterias().isEmpty()) {
            addInvalidMessage(context, CRITERIAS_FIELD_ID, Labels.getLabel(CRITERIAS_LEAST_ONE_VALIDATION_LABEL_KEY));
        }
    }

}
