package com.qsocialnow.viewmodel.subject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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

import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.services.SubjectService;

@VariableResolver(DelegatingVariableResolver.class)
@NotifyCommand(value = "modal$closeEvent", onChange = "_vm_.saved")
@ToClientCommand("modal$closeEvent")
public class EditSubjectViewModel implements Serializable {

    private static final long serialVersionUID = -8513141217044818510L;

    @WireVariable
    private SubjectService subjectService;

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
    }

    @Command
    @NotifyChange({ "currentSubject", "saved" })
    public void save() {
        Subject subject = new Subject();
        subject.setId(currentSubject.getSubject().getId());
        subject.setName(currentSubject.getSubject().getName());
        subject.setLastName(currentSubject.getSubject().getLastName());
        subject.setAge(currentSubject.getSubject().getAge());
        subject.setAddress(currentSubject.getSubject().getAddress());
        subject = subjectService.update(subject);
        Clients.showNotification(Labels.getLabel("subject.edit.notification.success",
                new String[] { subject.getName() }));

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
            args.put("subjectChanged", currentSubject);
            BindUtils.postGlobalCommand(null, null, "changeSubject", args);
        }
    }

}
