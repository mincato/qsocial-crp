package com.qsocialnow.validators;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;

import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.model.DomainView;

@Component
public class ResolutionListValidator extends AbstractValidator {

    @Override
    public void validate(ValidationContext context) {
    	Map<String,Property> beanProps = context.getProperties(context.getProperty().getBase());
        DomainView domainView = (DomainView)beanProps.get(".").getValue();
        if(domainView.getResolutions().isEmpty()) {
        	addInvalidMessage(context, "resolutions", Labels.getLabel("domain.resolutions.least.one.validation"));
        	return;
        }
        for (int i = 0; i < domainView.getResolutions().size() ; i++ ) {
        	Resolution resolution = domainView.getResolutions().get(i);
        	if (resolution.getDescription() == null || resolution.getDescription().isEmpty()) {
        		addInvalidMessage(context, "description_"+i, Labels.getLabel("domain.field.empty"));
        	}
        }
    }

}
