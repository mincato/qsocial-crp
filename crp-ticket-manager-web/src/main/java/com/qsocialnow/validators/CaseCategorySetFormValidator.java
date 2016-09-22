package com.qsocialnow.validators;

import java.text.MessageFormat;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;

import com.qsocialnow.viewmodel.casecategoryset.CaseCategorySetView;
import com.qsocialnow.viewmodel.casecategoryset.CaseCategoryView;

@Component
public class CaseCategorySetFormValidator extends AbstractValidator {

    private static final String DESCRIPTION_FIELD_ID_PATTERN = "description_{0}";

    private static final String CATEGORIES_FIELD_ID = "categories";

    private static final String CATEGORIES_LEAST_ONE_VALIDATION_LABEL_KEY = "casecategoryset.categories.least.one.validation";

    private static final String FIELD_EMPTY_LABEL_KEY = "casecategoryset.field.empty";

    @Override
    public void validate(ValidationContext context) {
        Map<String, Property> beanProps = context.getProperties(context.getProperty().getBase());
        CaseCategorySetView caseCategorySetView = (CaseCategorySetView) beanProps.get(".").getValue();
        if (caseCategorySetView.getCategories().isEmpty()) {
            addInvalidMessage(context, CATEGORIES_FIELD_ID, Labels.getLabel(CATEGORIES_LEAST_ONE_VALIDATION_LABEL_KEY));
            return;
        }
        for (int i = 0; i < caseCategorySetView.getCategories().size(); i++) {
            CaseCategoryView category = caseCategorySetView.getCategories().get(i);
            if (StringUtils.isBlank(category.getDescription())) {
                addInvalidMessage(context, MessageFormat.format(DESCRIPTION_FIELD_ID_PATTERN, i),
                        Labels.getLabel(FIELD_EMPTY_LABEL_KEY));
            }
        }
    }

}
