package com.qsocialnow.validators;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;

import com.qsocialnow.model.TriggerView;

@Component
public class TriggerFormValidator extends AbstractValidator {

    private static final String SEGMENTS_FIELD_ID = "segments";

    private static final String RESOLUTIONS_FIELD_ID = "resolutions";

    private static final String CASE_CATEGORY_SETS_FIELD_ID = "casecategorysets";

    private static final String SUBJECT_CATEGORY_SETS_FIELD_ID = "subjectcategorysets";

    private static final String SEGMENTS_LEAST_ONE_VALIDATION_LABEL_KEY = "trigger.segments.least.one.validation";

    private static final String RESOLUTIONS_LEAST_ONE_VALIDATION_LABEL_KEY = "trigger.resolutions.least.one.validation";

    private static final String CASE_CATEGORY_SETS_LEAST_ONE_VALIDATION_LABEL_KEY = "trigger.casecategorysets.least.one.validation";

    private static final String SUBJECT_CATEGORY_SETS_LEAST_ONE_VALIDATION_LABEL_KEY = "trigger.subjectcategorysets.least.one.validation";

    @Override
    public void validate(ValidationContext context) {
        Map<String, Property> beanProps = context.getProperties(context.getProperty().getBase());
        TriggerView triggerView = (TriggerView) beanProps.get(".").getValue();
        validateNotEmptyList(context, triggerView.getTrigger().getSegments(), SEGMENTS_LEAST_ONE_VALIDATION_LABEL_KEY,
                SEGMENTS_FIELD_ID);
        validateNotEmptyList(context, triggerView.getResolutions(), RESOLUTIONS_LEAST_ONE_VALIDATION_LABEL_KEY,
                RESOLUTIONS_FIELD_ID);
        validateNotEmptyList(context, triggerView.getCaseCategorySets(),
                CASE_CATEGORY_SETS_LEAST_ONE_VALIDATION_LABEL_KEY, CASE_CATEGORY_SETS_FIELD_ID);
        validateNotEmptyList(context, triggerView.getSubjectCategorySets(),
                SUBJECT_CATEGORY_SETS_LEAST_ONE_VALIDATION_LABEL_KEY, SUBJECT_CATEGORY_SETS_FIELD_ID);
    }

    private void validateNotEmptyList(ValidationContext context, @SuppressWarnings("rawtypes") List list, String label,
            String componentId) {
        if (list.isEmpty()) {
            addInvalidMessage(context, componentId, Labels.getLabel(label));
        }
    }

}
