package com.qsocialnow.viewmodel.trigger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.Status;
import com.qsocialnow.common.model.config.TriggerListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.converters.DateConverter;
import com.qsocialnow.model.DomainView;
import com.qsocialnow.services.DomainService;
import com.qsocialnow.services.TriggerService;
import com.qsocialnow.services.UserSessionService;

@VariableResolver(DelegatingVariableResolver.class)
public class TriggersViewModel implements Serializable {

    private static final long serialVersionUID = 9145343693641922196L;

    private static final int PAGE_SIZE_DEFAULT = 15;

    private static final int ACTIVE_PAGE_DEFAULT = 0;

    private static final String SELECT_ALL = "title";

    private int pageSize = PAGE_SIZE_DEFAULT;
    private int activePage = ACTIVE_PAGE_DEFAULT;

    private String domain;

    private DomainView currentDomain;

    private DateConverter dateConverter;

    @WireVariable
    private UserSessionService userSessionService;

    @WireVariable
    private TriggerService triggerService;

    @WireVariable
    private DomainService domainService;

    private boolean moreResults;

    private List<TriggerListView> triggers = new ArrayList<>();

    private String keyword;

    private List<String> statusOptions;

    private String status;

    private Long fromDate;

    private Long toDate;

    private boolean filterActive = false;

    @Init
    public void init(@QueryParam("domain") String domain) {
        this.dateConverter = new DateConverter(userSessionService.getTimeZone());
        this.domain = domain;
        this.currentDomain = new DomainView();
        this.currentDomain.setDomain(domainService.findOne(domain));
        this.statusOptions = Arrays.asList(Status.values()).stream().map(status -> status.name())
                .collect(Collectors.toList());
        this.statusOptions.add(0, SELECT_ALL);
        this.status = SELECT_ALL;
        findTriggers(this.domain);
    }

    public List<TriggerListView> getTriggers() {
        return this.triggers;
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

    public String getDomain() {
        return domain;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getStatusOptions() {
        return statusOptions;
    }

    public Long getFromDate() {
        return fromDate;
    }

    public void setFromDate(Long fromDate) {
        this.fromDate = fromDate;
    }

    public Long getToDate() {
        return toDate;
    }

    public void setToDate(Long toDate) {
        this.toDate = toDate;
    }

    public DateConverter getDateConverter() {
        return dateConverter;
    }

    @Command
    @NotifyChange({ "triggers", "moreResults" })
    public void moreResults() {
        this.activePage++;
        this.findTriggers(this.domain);
    }

    private PageResponse<TriggerListView> findTriggers(String domainId) {
        PageResponse<TriggerListView> pageResponse = triggerService.findAll(domainId, activePage, pageSize,
                getFilters());
        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.triggers.addAll(pageResponse.getItems());
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }

        return pageResponse;
    }

    @Command
    @NotifyChange({ "triggers", "moreResults", "filterActive" })
    public void search() {
        this.filterActive = checkFilterActive();
        this.setDefaultPage();
        this.triggers.clear();
        this.findTriggers(this.domain);
    }

    private boolean checkFilterActive() {
        return !StringUtils.isBlank(this.keyword) || !SELECT_ALL.equals(status) || fromDate != null || toDate != null;
    }

    private Map<String, String> getFilters() {
        if (!filterActive) {
            return null;
        }
        Map<String, String> filters = new HashMap<String, String>();
        if (!StringUtils.isBlank(this.keyword)) {
            filters.put("name", this.keyword);
        }
        if (this.status != null && !SELECT_ALL.equals(this.status)) {
            filters.put("status", this.status);
        }
        if (this.fromDate != null) {
            filters.put("fromDate", String.valueOf(this.fromDate));
        }
        if (this.toDate != null) {
            filters.put("toDate", String.valueOf(this.toDate));
        }
        return filters;
    }

    private void setDefaultPage() {
        this.pageSize = PAGE_SIZE_DEFAULT;
        this.activePage = ACTIVE_PAGE_DEFAULT;
    }

    public DomainView getCurrentDomain() {
        return currentDomain;
    }

    public void setCurrentDomain(DomainView currentDomain) {
        this.currentDomain = currentDomain;
    }

    public boolean isFilterActive() {
        return filterActive;
    }

}