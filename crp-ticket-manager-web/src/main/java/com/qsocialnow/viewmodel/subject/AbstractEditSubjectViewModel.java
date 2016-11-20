package com.qsocialnow.viewmodel.subject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Div;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.cases.ContactInfo;
import com.qsocialnow.common.model.cases.Person;
import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.common.model.config.Media;
import com.qsocialnow.common.model.config.SubjectCategory;
import com.qsocialnow.common.model.config.SubjectCategorySet;
import com.qsocialnow.common.model.event.PolygonLocation;
import com.qsocialnow.services.PersonService;
import com.qsocialnow.services.SubjectCategorySetService;
import com.qsocialnow.services.SubjectService;

@VariableResolver(DelegatingVariableResolver.class)
public class AbstractEditSubjectViewModel implements Serializable {

    private static final long serialVersionUID = -8513141217044818510L;

    @WireVariable
    private SubjectService subjectService;

    @WireVariable
    private PersonService personService;

    @WireVariable
    private SubjectCategorySetService subjectCategorySetService;

    private SubjectView currentSubject;

    private boolean saved;

    protected void initSubject(String subjectId) {
        Subject subject = subjectService.findOne(subjectId);
        Person person = personService.findOne(subject.getPersonId());

        currentSubject = new SubjectView();
        currentSubject.setSubject(subject);
        currentSubject.setPerson(person);

        currentSubject.setSource(Media.getByValue(currentSubject.getSubject().getSource()));
        currentSubject.setCategories(new ArrayList<SubjectCategoryView>());
        if (currentSubject.getPerson().getContactInfo() == null) {
            currentSubject.getPerson().setContactInfo(new ContactInfo());
        }
        if (!CollectionUtils.isEmpty(currentSubject.getSubject().getSubjectCategorySet())) {
            initCategories(currentSubject.getCategories(), currentSubject.getSubject().getSubjectCategorySet(),
                    currentSubject.getSubject().getSubjectCategory());

        }

    }

    private void initCategories(List<SubjectCategoryView> categoriesView, Set<String> setIds, Set<String> categoryIds) {

        for (String categorySetId : setIds) {
            SubjectCategorySet categorySet = subjectCategorySetService.findOne(categorySetId);
            List<SubjectCategory> categories = new ArrayList<SubjectCategory>();
            categories.addAll(categorySet.getCategories().stream()
                    .filter(category -> categoryIds.contains(category.getId())).collect(Collectors.toList()));
            for (SubjectCategory category : categories) {
                SubjectCategoryView categoryView = new SubjectCategoryView();
                categoryView.setSet(categorySet);
                categoryView.setCategory(category);
                categoriesView.add(categoryView);
            }
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public SubjectView getCurrentSubject() {
        return currentSubject;
    }

    public void setCurrentSubject(SubjectView currentSubject) {
        this.currentSubject = currentSubject;
    }

    public SubjectService getSubjectService() {
        return subjectService;
    }

    public PersonService getPersonService() {
        return personService;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    @Command
    public void close(@ContextParam(ContextType.VIEW) Div comp) {
        comp.detach();
    }

    @Command
    public String buildLocation(Subject subject) {
        String location = null;
        if (subject.getLocationMethod() != null) {
            switch (subject.getLocationMethod()) {
                case POLYGON_LOCATION:
                    PolygonLocation polygon = new GsonBuilder()
                            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
                            .fromJson(subject.getLocation(), PolygonLocation.class);
                    location = polygon.getFullName();
                    break;
                default:
                    location = subject.getLocation();
                    break;
            }

        }
        return location;
    }

}
