package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.HashMap;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.NotifyCommand;
import org.zkoss.bind.annotation.ToClientCommand;

import com.qsocialnow.viewmodel.subject.AbstractEditSubjectViewModel;

@NotifyCommand(value = "modal$closeEvent", onChange = "_vm_.saved")
@ToClientCommand("modal$closeEvent")
public class EditSubjectViewModel extends AbstractEditSubjectViewModel implements Serializable {

    private static final long serialVersionUID = 7336784863652145386L;

    @Init
    public void init(@BindingParam("subject") String subject) {
        initSubject(subject);
    }

    @Command
    @NotifyChange({ "currentSubject", "saved" })
    public void save() {
        HashMap<String, Object> args = new HashMap<>();
        args.put("subject", getCurrentSubject().getSubject());
        BindUtils.postGlobalCommand(null, null, "modifySubject", args);
        setSaved(true);
    }

}
