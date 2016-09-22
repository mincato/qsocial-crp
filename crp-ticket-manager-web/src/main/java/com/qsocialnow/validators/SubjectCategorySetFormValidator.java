package com.qsocialnow.validators;

import java.text.MessageFormat;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;

import com.qsocialnow.viewmodel.subjectcategoryset.SubjectCategorySetView;
import com.qsocialnow.viewmodel.subjectcategoryset.SubjectCategoryView;

@Component
public class SubjectCategorySetFormValidator extends AbstractValidator {

    private static final String DESCRIPTION_FIELD_ID_PATTERN = "description_{0}";

    private static final String CATEGORIES_FIELD_ID = "categories";

    private static final String CATEGORIES_LEAST_ONE_VALIDATION_LABEL_KEY = "subjectcategoryset.categories.least.one.validation";

    private static final String FIELD_EMPTY_LABEL_KEY = "subjectcategoryset.field.empty";

    @Override
    public void validate(ValidationContext context) {
        Map<String, Property> beanProps = context.getProperties(context.getProperty().getBase());
        SubjectCategorySetView subjectCategorySetView = (SubjectCategorySetView) beanProps.get(".").getValue();
        if (subjectCategorySetView.getCategories().isEmpty()) {
            addInvalidMessage(context, CATEGORIES_FIELD_ID, Labels.getLabel(CATEGORIES_LEAST_ONE_VALIDATION_LABEL_KEY));
            return;
        }
        for (int i = 0; i < subjectCategorySetView.getCategories().size(); i++) {
            SubjectCategoryView category = subjectCategorySetView.getCategories().get(i);
            if (StringUtils.isBlank(category.getDescription())) {
                addInvalidMessage(context, MessageFormat.format(DESCRIPTION_FIELD_ID_PATTERN, i),
                        Labels.getLabel(FIELD_EMPTY_LABEL_KEY));
            }
        }
    }

}
