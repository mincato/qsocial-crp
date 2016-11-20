package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.NotifyCommand;
import org.zkoss.bind.annotation.ToClientCommand;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Div;

import com.qsocialnow.common.model.cases.Person;
import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.common.model.cases.SubjectListView;
import com.qsocialnow.common.model.config.Media;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.services.SubjectService;

@VariableResolver(DelegatingVariableResolver.class)
@NotifyCommand(value = "modal$closeEvent", onChange = "_vm_.selected")
@ToClientCommand("modal$closeEvent")
public class ChooseManualSubjectViewModel implements Serializable {

    private static final long serialVersionUID = 6552857606654418645L;

    private static final int PAGE_SIZE_DEFAULT = 15;

    private static final int ACTIVE_PAGE_DEFAULT = 0;

    private int pageSize = PAGE_SIZE_DEFAULT;
    private int activePage = ACTIVE_PAGE_DEFAULT;

    @WireVariable
    private SubjectService subjectService;

    private boolean moreResults;

    private List<SubjectListView> subjects = new ArrayList<>();

    private String keyword;

    private boolean selected = false;

    private Subject subject;

    private Person person;

    private String source;

    private boolean enabledCreateSubject = false;

    @Init
    public void init(@BindingParam("source") String source) {
        this.source = source;
        findSubjects();
    }

    public List<SubjectListView> getSubjects() {
        return subjects;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public boolean isMoreResults() {
        return moreResults;
    }

    public boolean isSelected() {
        return selected;
    }

    @Command
    @NotifyChange({ "subjects", "moreResults" })
    public void moreResults() {
        this.activePage++;
        this.findSubjects();
    }

    @Command
    @NotifyChange("selected")
    public void selectSubject(@BindingParam("subject") SubjectListView subject) {
        this.subject = new Subject();
        this.subject.setId(subject.getId());
        this.subject.setIdentifier(subject.getIdentifier());
        this.subject.setSource(subject.getSource());
        this.subject.setSourceName(subject.getSourceName());
        selected = true;
    }

    @Command
    public void close(@ContextParam(ContextType.VIEW) Div comp) {
        comp.detach();
        if (selected) {
            Map<String, Object> args = new HashMap<String, Object>();
            args.put("subject", subject);
            BindUtils.postGlobalCommand(null, null, "changeSubject", args);
        }
    }

    private PageResponse<SubjectListView> findSubjects() {
        PageResponse<SubjectListView> pageResponse = subjectService.findAll(activePage, pageSize, getFilters());
        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.subjects.addAll(pageResponse.getItems());
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }

        return pageResponse;
    }

    @Command
    @NotifyChange({ "subjects", "moreResults", "filterActive" })
    public void search() {
        this.setDefaultPage();
        this.subjects.clear();
        this.findSubjects();
    }

    private Map<String, String> getFilters() {
        Map<String, String> filters = new HashMap<String, String>();
        if (!StringUtils.isBlank(this.keyword)) {
            filters.put("identifier", this.keyword);
        }
        if (this.source != null) {
            filters.put("source", source);
        }
        return filters;
    }

    private void setDefaultPage() {
        this.pageSize = PAGE_SIZE_DEFAULT;
        this.activePage = ACTIVE_PAGE_DEFAULT;
    }

    @Command
    @NotifyChange({ "enabledCreateSubject", "subject" })
    public void initCreateSubject() {
        this.enabledCreateSubject = true;
        this.subject = new Subject();
        this.subject.setSource(Media.MANUAL.getValue());
        this.person = new Person();
    }

    @Command
    @NotifyChange({ "enabledCreateSubject" })
    public void cancelCreateSubject() {
        this.enabledCreateSubject = false;
        this.subject = null;
    }

    public boolean isEnabledCreateSubject() {
        return enabledCreateSubject;
    }

    public void setEnabledCreateSubject(boolean enabledCreateSubject) {
        this.enabledCreateSubject = enabledCreateSubject;
    }

    @Command
    @NotifyChange("selected")
    public void createSubject() {
        this.selected = true;
    }

    public Validator getFormValidator() {
        final ChooseManualSubjectViewModel vm = this;
        return new AbstractValidator() {

            private static final String EMPTY_FIELD_KEY_LABEL = "app.field.empty.validation";

            private static final String SOURCE_NAME_FIELD_ID = "sourceName";

            private static final String IDENTIFIER_FIELD_ID = "identifier";

            private static final String SOURCE_NAME_UNIQUE_VALIDATION_LABEL = "cases.subject.sourcename.unique.validation";

            private static final String IDENTIFIER_UNIQUE_VALIDATION_LABEL = "cases.subject.identifier.unique.validation";

            public void validate(ValidationContext context) {
                Map<String, Property> beanProps = context.getProperties(context.getProperty().getBase());
                Subject subject = (Subject) beanProps.get(".").getValue();
                if (StringUtils.isEmpty(subject.getSourceName())) {
                    addInvalidMessage(context, SOURCE_NAME_FIELD_ID, Labels.getLabel(EMPTY_FIELD_KEY_LABEL));
                } else {
                    Map<String, String> filters = new HashMap<String, String>();
                    filters.put("sourceName", subject.getSourceName());
                    filters.put("source", Media.MANUAL.getValue().toString());
                    PageResponse<SubjectListView> pageResponse = vm.subjectService.findAll(0, 1, filters);
                    if (!CollectionUtils.isEmpty(pageResponse.getItems())) {
                        addInvalidMessage(context, SOURCE_NAME_FIELD_ID,
                                Labels.getLabel(SOURCE_NAME_UNIQUE_VALIDATION_LABEL));
                    }

                }
                if (StringUtils.isEmpty(subject.getIdentifier())) {
                    addInvalidMessage(context, IDENTIFIER_FIELD_ID, Labels.getLabel(EMPTY_FIELD_KEY_LABEL));
                } else {
                    Map<String, String> filters = new HashMap<String, String>();
                    filters.put("identifier", subject.getIdentifier());
                    filters.put("source", Media.MANUAL.getValue().toString());
                    PageResponse<SubjectListView> pageResponse = vm.subjectService.findAll(0, 1, filters);
                    if (!CollectionUtils.isEmpty(pageResponse.getItems())) {
                        addInvalidMessage(context, IDENTIFIER_FIELD_ID,
                                Labels.getLabel(IDENTIFIER_UNIQUE_VALIDATION_LABEL));
                    }

                }

            }
        };
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
