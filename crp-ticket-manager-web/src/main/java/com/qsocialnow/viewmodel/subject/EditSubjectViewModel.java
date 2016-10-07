package com.qsocialnow.viewmodel.subject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.NotifyCommand;
import org.zkoss.bind.annotation.ToClientCommand;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Div;

import com.qsocialnow.common.model.cases.ContactInfo;
import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.common.model.config.Media;
import com.qsocialnow.common.model.config.SubjectCategory;
import com.qsocialnow.common.model.config.SubjectCategorySet;
import com.qsocialnow.services.SubjectCategorySetService;
import com.qsocialnow.services.SubjectService;

@VariableResolver(DelegatingVariableResolver.class)
@NotifyCommand(value = "modal$closeEvent", onChange = "_vm_.saved")
@ToClientCommand("modal$closeEvent")
public class EditSubjectViewModel implements Serializable {

    private static final long serialVersionUID = -8513141217044818510L;

    @WireVariable
    private SubjectService subjectService;

    @WireVariable
    private SubjectCategorySetService subjectCategorySetService;

    private String subjectId;

    private SubjectView currentSubject;

    private boolean saved;

    @Init
    public void init(@BindingParam("subject") String subject) {
        subjectId = subject;
        initSubject(subjectId);
    }

    private void initSubject(String subjectId) {
        Subject subject = subjectService.findOne(subjectId);
        currentSubject = new SubjectView();
        currentSubject.setSubject(subject);
        currentSubject.setSource(Media.getByValue(currentSubject.getSubject().getSource()));
        currentSubject.setCategories(new ArrayList<SubjectCategoryView>());
        if (currentSubject.getSubject().getContactInfo() == null) {
            currentSubject.getSubject().setContactInfo(new ContactInfo());
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

    @Command
    @NotifyChange({ "currentSubject", "saved" })
    public void save() {
        Subject subject = subjectService.update(currentSubject.getSubject());
        currentSubject.setSubject(subject);
        Clients.showNotification(Labels.getLabel("subject.edit.notification.success",
                new String[] { subject.getIdentifier() }));

        saved = true;
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

    @Command
    public void close(@ContextParam(ContextType.VIEW) Div comp) {
        comp.detach();
        if (saved) {
            Map<String, Object> args = new HashMap<String, Object>();
            args.put("subjectChanged", currentSubject.getSubject());
            BindUtils.postGlobalCommand(null, null, "changeSubject", args);
        }
    }

}
