package com.qsocialnow.common.model.cases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.validator.constraints.NotBlank;

import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.config.BaseUser;
import com.qsocialnow.common.model.config.BaseUserResolver;
import com.qsocialnow.common.model.event.Event;

public class Case implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String domainId;

    private String triggerId;

    private String segmentId;

    private Boolean open;

    private Boolean pendingResponse;

    private Long openDate;

    private Long closeDate;

    private Long lastModifiedTimestamp;

    @NotBlank(message = "{field.empty}")
    private String title;

    @NotBlank(message = "{field.empty}")
    private String description;

    private Event triggerEvent;

    private Coordinates coordinates;

    private Coordinates adminUnits;

    private String resolution;

    private Integer asignedValue;

    private String unitValue;

    private Subject subject;

    private Set<String> caseCategories;

    private Set<String> caseCategoriesSet;

    private List<ActionRegistry> actionsRegistry;

    private BaseUserResolver userResolver;

    private String lastPostId;

    private String idRootComment;

    private Long source;

    private BaseUser assignee;

    private String teamId;

    private Set<String> attachments;

    private List<Message> messages;

    private Priority priority;

    public Case() {

    }

    public static Case getNewCaseFromEvent(Event event) {
        Case newCase = new Case();
        newCase.setOpen(true);

        Long openDate = new Date().getTime();
        newCase.setOpenDate(openDate);
        newCase.setTitle(event.getTitulo());
        newCase.setPriority(Priority.MEDIUM);

        newCase.setPendingResponse(true);
        newCase.setLastPostId(event.getId());
        newCase.setSource(event.getMedioId());
        Message message = new Message();
        message.setId(event.getId());
        message.setFromResponseDetector(event.isResponseDetected());
        newCase.addMessage(message);

        // creating first registry
        List<ActionRegistry> registries = new ArrayList<>();
        ActionRegistry registry = new ActionRegistry();
        registry.setAction(ActionType.OPEN_CASE.name());
        registry.setComment(event.getUsuarioOriginal() + " - " + event.getTitulo());
        registry.setAutomatic(true);
        registry.setDate(openDate);

        registry.setEvent(event);

        registries.add(registry);
        newCase.setActionsRegistry(registries);

        return newCase;
    }

    public static Case getNewCase() {
        Case newCase = new Case();
        newCase.setOpen(true);

        Long openDate = new Date().getTime();
        newCase.setOpenDate(openDate);
        newCase.setPendingResponse(true);
        newCase.setPriority(Priority.MEDIUM);

        // creating first registry
        List<ActionRegistry> registries = new ArrayList<>();
        ActionRegistry registry = new ActionRegistry();
        registry.setAction(ActionType.OPEN_CASE.name());
        registry.setComment(null);
        registry.setAutomatic(true);
        registry.setDate(openDate);

        registries.add(registry);
        newCase.setActionsRegistry(registries);
        return newCase;
    }

    public List<ActionType> getAllowedManualActions() {
        List<ActionType> actionsAllowed = new ArrayList<>();

        if (this.getOpen()) {
            actionsAllowed.add(ActionType.TAG_CASE);
            actionsAllowed.add(ActionType.CLOSE);
            actionsAllowed.add(ActionType.REGISTER_COMMENT);
            actionsAllowed.add(ActionType.MODIFY);
            if (!this.getPendingResponse())
                actionsAllowed.add(ActionType.PENDING_RESPONSE);

            if (this.getSubject() != null) {
                actionsAllowed.add(ActionType.REPLY);
                actionsAllowed.add(ActionType.SEND_MESSAGE);
                actionsAllowed.add(ActionType.TAG_SUBJECT);
            }
            actionsAllowed.add(ActionType.CHANGE_SUBJECT);
            actionsAllowed.add(ActionType.ASSIGN);
            actionsAllowed.add(ActionType.RESOLVE);
            actionsAllowed.add(ActionType.ATTACH_FILE);
            actionsAllowed.add(ActionType.CHANGE_PRIORITY);
        } else {
            actionsAllowed.add(ActionType.REOPEN);
        }
        return actionsAllowed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public Boolean getPendingResponse() {
        return pendingResponse;
    }

    public void setPendingResponse(Boolean pendingResponse) {
        this.pendingResponse = pendingResponse;
    }

    public Long getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Long openDate) {
        this.openDate = openDate;
    }

    public Long getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Long closeDate) {
        this.closeDate = closeDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Event getTriggerEvent() {
        return triggerEvent;
    }

    public void setTriggerEvent(Event triggerEvent) {
        this.triggerEvent = triggerEvent;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getResolution() {
        return resolution;
    }

    public Coordinates getAdminUnits() {
        return adminUnits;
    }

    public void setAdminUnits(Coordinates adminUnits) {
        this.adminUnits = adminUnits;
    }

    public Integer getAsignedValue() {
        return asignedValue;
    }

    public void setAsignedValue(Integer asignedValue) {
        this.asignedValue = asignedValue;
    }

    public String getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(String unitValue) {
        this.unitValue = unitValue;
    }

    public Set<String> getCaseCategories() {
        return caseCategories;
    }

    public void setCaseCategories(Set<String> caseCategories) {
        this.caseCategories = caseCategories;
    }

    public List<ActionRegistry> getActionsRegistry() {
        return actionsRegistry;
    }

    public void setActionsRegistry(List<ActionRegistry> actionsRegistry) {
        this.actionsRegistry = actionsRegistry;
    }

    public void setTriggerId(String triggerId) {
        this.triggerId = triggerId;
    }

    public String getTriggerId() {
        return triggerId;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public BaseUserResolver getUserResolver() {
        return userResolver;
    }

    public void setUserResolver(BaseUserResolver userResolver) {
        this.userResolver = userResolver;
    }

    public String getLastPostId() {
        return lastPostId;
    }

    public void setLastPostId(String lastPostId) {
        this.lastPostId = lastPostId;
    }

    public String getIdRootComment() {
        return idRootComment;
    }

    public void setIdRootComment(String idRootComment) {
        this.idRootComment = idRootComment;
    }

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public Set<String> getCaseCategoriesSet() {
        return caseCategoriesSet;
    }

    public void setCaseCategoriesSet(Set<String> caseCategoriesSet) {
        this.caseCategoriesSet = caseCategoriesSet;
    }

    public Long getSource() {
        return source;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public BaseUser getAssignee() {
        return assignee;
    }

    public void setAssignee(BaseUser assignee) {
        this.assignee = assignee;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public Set<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<String> attachments) {
        this.attachments = attachments;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Long getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public void setLastModifiedTimestamp(Long lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    public void addMessage(Message message) {
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(message);

    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

}
