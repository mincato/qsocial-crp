package com.qsocialnow.common.model.cases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.config.BaseUserResolver;
import com.qsocialnow.common.model.event.InPutBeanDocument;

public class Case implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String triggerId;

    private Boolean open;

    private Boolean pendingResponse;

    private Date openDate;

    private Date closeDate;

    private String title;

    private String description;

    private Event triggerEvent;

    private Coordinates coordinates;

    private Coordinates adminUnits;

    private String resolution;

    private Integer asignedValue;

    private String unitValue;

    private Customer customer;

    private List<Long> caseCategories;

    private List<ActionRegistry> actionsRegistry;

    private String teamId;

    private BaseUserResolver userResolver;

    private String sourceUser;

    private String lastPostId;

    public Case() {

    }

    public static Case getNewCaseFromEvent(InPutBeanDocument event) {
        Case newCase = new Case();
        newCase.setOpen(true);

        Date openDate = new Date();
        newCase.setOpenDate(openDate);
        newCase.setTitle(event.getTitulo());

        newCase.setCaseCategories(Arrays.asList(event.getCategorias()));
        newCase.setPendingResponse(true);
        newCase.setSourceUser(event.getUsuarioCreacion());
        newCase.setLastPostId(event.getId());

        // creating first registry
        List<ActionRegistry> registries = new ArrayList<>();
        ActionRegistry registry = new ActionRegistry();
        registry.setAction(ActionType.OPEN_CASE.name());
        registry.setComment("Id: " + event.getId() + " - " + event.getTitulo());
        registry.setAutomatic(true);
        registry.setDate(openDate);

        Event originEvent = new Event();
        originEvent.setId(event.getId());
        originEvent.setDescription(event.getName());
        registry.setEvent(originEvent);

        registries.add(registry);
        newCase.setActionsRegistry(registries);

        return newCase;
    }

    public List<ActionType> getAllowedManualActions() {
        List<ActionType> actionsAllowed = new ArrayList<>();

        if (this.getOpen()) {
            actionsAllowed.add(ActionType.REPLY);
            // actionsAllowed.add(ActionType.TAG_SUBJECT);
            // actionsAllowed.add(ActionType.TAG_CASE);
            actionsAllowed.add(ActionType.CLOSE);
            actionsAllowed.add(ActionType.REGISTER_COMMENT);
            actionsAllowed.add(ActionType.MODIFY);
            if (!this.getPendingResponse())
                actionsAllowed.add(ActionType.PENDING_RESPONSE);

            actionsAllowed.add(ActionType.SEND_MESSAGE);
            actionsAllowed.add(ActionType.ASSIGN);
            actionsAllowed.add(ActionType.RESOLVE);
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

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public List<Long> getCaseCategories() {
        return caseCategories;
    }

    public void setCaseCategories(List<Long> caseCategories) {
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

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public BaseUserResolver getUserResolver() {
        return userResolver;
    }

    public void setUserResolver(BaseUserResolver userResolver) {
        this.userResolver = userResolver;
    }

    public String getSourceUser() {
        return sourceUser;
    }

    public void setSourceUser(String sourceUser) {
        this.sourceUser = sourceUser;
    }

    public String getLastPostId() {
        return lastPostId;
    }

    public void setLastPostId(String lastPostId) {
        this.lastPostId = lastPostId;
    }

}
