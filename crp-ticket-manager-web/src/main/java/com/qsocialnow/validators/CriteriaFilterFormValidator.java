package com.qsocialnow.validators;

import java.text.MessageFormat;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;

import com.qsocialnow.common.model.config.WordFilter;
import com.qsocialnow.model.CategoryFilterView;
import com.qsocialnow.model.FilterView;

public class CriteriaFilterFormValidator extends AbstractValidator {

	private static final String FILTERWORD_TEXT_FIELD_ID_PATTERN = "filterword_text_{0}";

	private static final String FILTERWORD_TYPE_FIELD_ID_PATTERN = "filterword_type_{0}";

	private static final String FILTERCATEGORY_FIELD_ID_PATTERN = "filtercategory_{0}";

	private static final String FIELD_EMPTY_LABEL_KEY = "app.field.empty.validation";

	@Override
	public void validate(ValidationContext context) {
		Map<String, Property> beanProps = context.getProperties(context
				.getProperty().getBase());
		FilterView filter = (FilterView) beanProps.get(".").getValue();
		if (!CollectionUtils.isEmpty(filter.getFilterWords())) {
			for (int i = 0; i < filter.getFilterWords().size(); i++) {
				WordFilter wordFilter = filter.getFilterWords().get(i);
				if (StringUtils.isBlank(wordFilter.getInputText())) {
					addInvalidMessage(context, MessageFormat.format(
							FILTERWORD_TEXT_FIELD_ID_PATTERN, i),
							Labels.getLabel(FIELD_EMPTY_LABEL_KEY));
				}
				if (wordFilter.getType() == null) {
					addInvalidMessage(context, MessageFormat.format(
							FILTERWORD_TYPE_FIELD_ID_PATTERN, i),
							Labels.getLabel(FIELD_EMPTY_LABEL_KEY));
				}

			}
		}
		if (!CollectionUtils.isEmpty(filter.getFilterCategories())) {
			for (int i = 0; i < filter.getFilterCategories().size(); i++) {
				CategoryFilterView category = filter.getFilterCategories().get(i);
				if (category.getCategoryGroup() == null) {
					addInvalidMessage(context, MessageFormat.format(
							FILTERCATEGORY_FIELD_ID_PATTERN, i),
							Labels.getLabel(FIELD_EMPTY_LABEL_KEY));
				}

			}
		}
	}
}
