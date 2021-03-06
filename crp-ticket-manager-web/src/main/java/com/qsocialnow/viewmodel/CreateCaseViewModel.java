package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.common.model.config.AdminUnit;
import com.qsocialnow.common.model.config.BaseAdminUnit;
import com.qsocialnow.common.model.config.DomainListView;
import com.qsocialnow.common.model.config.Media;
import com.qsocialnow.common.model.config.SegmentListView;
import com.qsocialnow.common.model.config.TriggerListView;
import com.qsocialnow.common.model.event.Event;
import com.qsocialnow.common.model.event.EventLocation;
import com.qsocialnow.common.model.event.GeoSocialEventLocationMethod;
import com.qsocialnow.handler.NotificationHandler;
import com.qsocialnow.model.CaseView;
import com.qsocialnow.services.AutocompleteService;
import com.qsocialnow.services.CaseService;
import com.qsocialnow.services.DomainService;
import com.qsocialnow.services.TriggerService;
import com.qsocialnow.services.UserSessionService;

@VariableResolver(DelegatingVariableResolver.class)
public class CreateCaseViewModel implements Serializable {

    private static final long serialVersionUID = 2259179419421396093L;

    @WireVariable
    private CaseService caseService;

    @WireVariable
    private DomainService domainService;

    @WireVariable
    private TriggerService triggerService;

    @WireVariable
    private AutocompleteService<AdminUnit> adminUnitsAutocompleteService;

    @WireVariable
    private UserSessionService userSessionService;

    private CaseView currentCase;

    private List<DomainListView> domains = new ArrayList<DomainListView>();

    private List<TriggerListView> triggers;

    private List<SegmentListView> segments;

    private AutocompleteListModel<AdminUnit> adminUnits;

    public CaseView getCurrentCase() {
        return currentCase;
    }

    @Init
    public void init() {
        initDomains();
        initCase();
        this.adminUnits = new AutocompleteListModel<AdminUnit>(adminUnitsAutocompleteService,
                userSessionService.getLanguage());
    }

    private void initDomains() {
        this.domains = domainService.findAllActive();
    }

    private void initCase() {
        currentCase = new CaseView();
        currentCase.setNewCase(Case.getNewCase());
    }

    @Command
    @NotifyChange("triggers")
    public void onSelectDomain(@BindingParam("domain") DomainListView domain) {
        this.triggers = triggerService.findAllActive(domain.getId());
    }

    @Command
    @NotifyChange("segments")
    public void onSelectTrigger(@BindingParam("domain") DomainListView domain,
            @BindingParam("trigger") TriggerListView trigger) {
        this.segments = triggerService.findActiveSegments(domain.getId(), trigger.getId());
    }

    @Command
    @NotifyChange("currentCase")
    public void save() {
        Case newCase = currentCase.getNewCase();

        newCase.getTriggerEvent().setTitulo(newCase.getTitle());
        newCase.getActionsRegistry().get(0).getEvent().setTitulo(newCase.getTitle());
        newCase.setSubject(currentCase.getSelectedSubject());

        if (currentCase.getAdminUnit() != null) {
            fillAdmUnit(currentCase.getAdminUnit(), newCase.getTriggerEvent());
        }
        if (currentCase.getLatitude() != null && currentCase.getLongitude() != null) {
            EventLocation eventLocation = new EventLocation();
            eventLocation.setLat(currentCase.getLatitude());
            eventLocation.setLon(currentCase.getLongitude());
            newCase.getTriggerEvent().setLocation(eventLocation);
            newCase.getTriggerEvent().setLocationMethod(GeoSocialEventLocationMethod.GEO_LOCATION);
        }

        newCase.setDomainId(currentCase.getSelectedDomain().getId());
        newCase.setTriggerId(currentCase.getSelectedTrigger().getId());
        newCase.setSegmentId(currentCase.getSelectedSegment().getId());

        if (currentCase.getSelectedSegment() != null)
            newCase.setTeamId(currentCase.getSelectedSegment().getTeamId());

        currentCase.setNewCase(caseService.create(newCase));

        NotificationHandler.addNotification(Labels.getLabel("cases.create.notification.success",
                new String[] { currentCase.getNewCase().getTitle() }));
        Executions.getCurrent().sendRedirect("/pages/cases/list/index.zul");
    }

    private void fillAdmUnit(AdminUnit adminUnit, Event event) {
        List<BaseAdminUnit> adminUnits = new ArrayList<BaseAdminUnit>();
        if (!CollectionUtils.isEmpty(adminUnit.getParents())) {
            adminUnits.addAll(adminUnit.getParents());
        }
        adminUnits.add(adminUnit);

        for (BaseAdminUnit item : adminUnits) {
            switch (item.getType()) {
                case CONTINENT:
                    event.setContinent(item.getGeoNameId());
                    break;
                case COUNTRY:
                    event.setCountry(item.getGeoNameId());
                    break;
                case CITY:
                    event.setCity(item.getGeoNameId());
                    break;
                case NEIGHBORHOOD:
                    event.setNeighborhood(item.getGeoNameId());
                    break;
                case ADM1:
                    event.setAdm1(item.getGeoNameId());
                    break;
                case ADM2:
                    event.setAdm2(item.getGeoNameId());
                    break;
                case ADM3:
                    event.setAdm3(item.getGeoNameId());
                    break;
                case ADM4:
                    event.setAdm4(item.getGeoNameId());
                    break;
                default:
                    break;
            }
        }

    }

    @Command
    @NotifyChange({ "currentCase" })
    public void clear() {
        initCase();
    }

    @Command
    public String createAdmUnitValue(AdminUnit adminUnit) {
        StringBuilder sb = new StringBuilder();
        addAdmUnitText(sb, adminUnit);
        return sb.toString();
    }

    @Command
    public String createAdmUnitDescription(AdminUnit adminUnit) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < adminUnit.getParents().size(); i++) {
            BaseAdminUnit baseAdminUnit = adminUnit.getParents().get(i);
            if (i > 0 && i < adminUnit.getParents().size() - 1) {
                sb.append(" - ");
            }
            addAdmUnitText(sb, baseAdminUnit);
        }
        return sb.toString();
    }

    private void addAdmUnitText(StringBuilder sb, BaseAdminUnit adminUnit) {
        sb.append(adminUnit.getTranslation());
        sb.append("(");
        sb.append(Labels.getLabel("trigger.criteria.admUnit.value." + adminUnit.getType().name()));
        sb.append(")");
    }

    public List<DomainListView> getDomains() {
        return domains;
    }

    public List<TriggerListView> getTriggers() {
        return triggers;
    }

    public List<SegmentListView> getSegments() {
        return segments;
    }

    public AutocompleteListModel<AdminUnit> getAdminUnits() {
        return adminUnits;
    }

    public void setAdminUnits(AutocompleteListModel<AdminUnit> adminUnits) {
        this.adminUnits = adminUnits;
    }

    private CaseView fxCaseView = null;

    @Command
    public void selectSubject(@BindingParam("newCase") CaseView newCase) {
        this.fxCaseView = newCase;
        Map<String, Object> params = new HashMap<>();
        params.put("source", Media.MANUAL.getValue());
        Executions.createComponents("/pages/cases/create/choose-manual-subject.zul", null, params);
    }

    @GlobalCommand
    public void changeSubject(@BindingParam("subject") Subject subject) {
        this.fxCaseView.setSelectedSubject(subject);
        BindUtils.postNotifyChange(null, null, this.fxCaseView, "selectedSubject");
        this.fxCaseView = null;
    }

    public Validator getFormValidator() {
        return new AbstractValidator() {

            private static final String EMPTY_FIELD_KEY_LABEL = "app.field.empty.validation";

            private static final String SUBJECT_FIELD_ID = "subject";

            public void validate(ValidationContext context) {
                Map<String, Property> beanProps = context.getProperties(context.getProperty().getBase());
                CaseView caseView = (CaseView) beanProps.get(".").getValue();
                if (caseView.getSelectedSubject() == null) {
                    addInvalidMessage(context, SUBJECT_FIELD_ID, Labels.getLabel(EMPTY_FIELD_KEY_LABEL));
                }
            }
        };
    }

    @Command
    public void selectCoordinates(@BindingParam("newCase") CaseView newCase) {
        this.fxCaseView = newCase;
        Executions.createComponents("/pages/cases/create/choose-coordinates.zul", null, null);
    }

    @GlobalCommand
    public void changeCoordinates(@BindingParam("latitude") Double latitude, @BindingParam("longitude") Double longitude) {
        this.fxCaseView.setLatitude(latitude);
        this.fxCaseView.setLongitude(longitude);
        BindUtils.postNotifyChange(null, null, this.fxCaseView, "latitude");
        BindUtils.postNotifyChange(null, null, this.fxCaseView, "longitude");
        this.fxCaseView = null;
    }

}