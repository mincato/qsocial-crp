package com.qsocialnow.validators;

import java.text.MessageFormat;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;

import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.model.DomainView;

@Component
public class ResolutionListValidator extends AbstractValidator {

	private static final String DESCRIPTION_FIELD_ID_PATTERN = "description_{0}";
	
	private static final String RESOLUTIONS_FIELD_ID = "resolutions";
	
	private static final String RESOLUTIONS_LEAST_ONE_VALIDATION_LABEL_KEY = "domain.resolutions.least.one.validation";
	
	private static final String FIELD_EMPTY_LABEL_KEY = "domain.field.empty";
	
    @Override
    public void validate(ValidationContext context) {
        Map<String, Property> beanProps = context.getProperties(context.getProperty().getBase());
        DomainView domainView = (DomainView) beanProps.get(".").getValue();
        if (domainView.getResolutions().isEmpty()) {
            addInvalidMessage(context, RESOLUTIONS_FIELD_ID, Labels.getLabel(RESOLUTIONS_LEAST_ONE_VALIDATION_LABEL_KEY));
            return;
        }
        for (int i = 0; i < domainView.getResolutions().size(); i++) {
            Resolution resolution = domainView.getResolutions().get(i);
            if (StringUtils.isBlank(resolution.getDescription())) {
                addInvalidMessage(context, MessageFormat.format(DESCRIPTION_FIELD_ID_PATTERN, i), Labels.getLabel(FIELD_EMPTY_LABEL_KEY));
            }
        }
    }

}
