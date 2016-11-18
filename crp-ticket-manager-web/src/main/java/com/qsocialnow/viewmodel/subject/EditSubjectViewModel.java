package com.qsocialnow.viewmodel.subject;

import java.io.Serializable;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.NotifyCommand;
import org.zkoss.bind.annotation.ToClientCommand;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.util.Clients;

import com.qsocialnow.common.model.cases.Person;
import com.qsocialnow.common.model.cases.Subject;

@NotifyCommand(value = "modal$closeEvent", onChange = "_vm_.saved")
@ToClientCommand("modal$closeEvent")
public class EditSubjectViewModel extends AbstractEditSubjectViewModel implements Serializable {

    private static final long serialVersionUID = -8513141217044818510L;

    @Init
    public void init(@BindingParam("subject") String subject) {
        initSubject(subject);
    }

    @Command
    @NotifyChange({ "currentSubject", "saved" })
    public void save() {
        Subject subject = getSubjectService().update(getCurrentSubject().getSubject());
        getCurrentSubject().setSubject(subject);
        Person person = getPersonService().update(getCurrentSubject().getPerson());
        getCurrentSubject().setPerson(person);
        Clients.showNotification(Labels.getLabel("subject.edit.notification.success",
                new String[] { subject.getIdentifier() }));

        setSaved(true);
    }

}
