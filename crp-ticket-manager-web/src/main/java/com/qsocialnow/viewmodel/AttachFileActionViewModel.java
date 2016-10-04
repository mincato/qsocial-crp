package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.ActionRequest;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.model.AttachFileActionView;
import com.qsocialnow.model.EditCaseView;
import com.qsocialnow.services.CaseService;
import com.qsocialnow.services.FileService;

@VariableResolver(DelegatingVariableResolver.class)
public class AttachFileActionViewModel implements Serializable {

    private static final long serialVersionUID = 6188170388292392609L;

    @WireVariable
    private CaseService caseService;

    @WireVariable
    private FileService fileService;

    private String caseId;

    private AttachFileActionView attachFileAction;

    private List<Media> files;

    private EditCaseView currentCase;

    @Init
    public void init(@QueryParam("case") String caseId) {
        attachFileAction = new AttachFileActionView();
        attachFileAction.setFiles(new HashSet<>());
        files = new ArrayList<>();
        this.caseId = caseId;
    }

    public AttachFileActionView getAttachFileAction() {
        return attachFileAction;
    }

    public void setAttachFileAction(AttachFileActionView attachFileAction) {
        this.attachFileAction = attachFileAction;
    }

    public List<Media> getFiles() {
        return files;
    }

    @GlobalCommand
    @NotifyChange({ "attachFileAction", "files" })
    public void show(@BindingParam("currentCase") EditCaseView currentCase, @BindingParam("action") ActionType action) {
        if (ActionType.ATTACH_FILE.equals(action)) {
            this.currentCase = currentCase;
            attachFileAction = new AttachFileActionView();
            if (CollectionUtils.isNotEmpty(currentCase.getCaseObject().getAttachments())) {
                attachFileAction.setFiles(new HashSet<>(currentCase.getCaseObject().getAttachments()));
            } else {
                attachFileAction.setFiles(new HashSet<>());
            }
            files = new ArrayList<>();
        }
    }

    @Command
    @NotifyChange({ "files" })
    public void upload(@BindingParam("files") Media[] files,
            @BindingParam("fxAttachFileAction") AttachFileActionView fxAttachFileActionView) {
        this.files.addAll(Arrays.asList(files));
        fxAttachFileActionView.getFiles().addAll(Arrays.stream(files).map(Media::getName).collect(Collectors.toList()));
    }

    @Command
    @NotifyChange({ "files" })
    public void removeFile(@BindingParam("file") String file,
            @BindingParam("fxAttachFileAction") AttachFileActionView fxAttachFileActionView) {
        Optional<Media> optionalMedia = this.files.stream().filter(media -> media.getName().equals(file)).findFirst();
        if (optionalMedia.isPresent()) {
            this.files.remove(optionalMedia.get());
        }
        fxAttachFileActionView.getFiles().remove(file);
    }

    @Command
    public void execute() {
        ActionRequest actionRequest = new ActionRequest();
        actionRequest.setActionType(ActionType.ATTACH_FILE);
        fileService.upload(files, currentCase.getCaseObject());
        Map<ActionParameter, Object> parameters = new HashMap<>();
        parameters.put(ActionParameter.FILES, attachFileAction.getFiles());
        actionRequest.setParameters(parameters);
        Case caseUpdated = caseService.executeAction(caseId, actionRequest);

        HashMap<String, Object> args = new HashMap<>();
        args.put("caseUpdated", caseUpdated);
        BindUtils.postGlobalCommand(null, null, "actionExecuted", args);
    }

}
