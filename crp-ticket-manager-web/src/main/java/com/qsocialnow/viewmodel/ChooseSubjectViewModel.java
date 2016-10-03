package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.NotifyCommand;
import org.zkoss.bind.annotation.ToClientCommand;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Div;

import com.qsocialnow.common.model.cases.SubjectListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.services.SubjectService;

@VariableResolver(DelegatingVariableResolver.class)
@NotifyCommand(value = "modal$closeEvent", onChange = "_vm_.selected")
@ToClientCommand("modal$closeEvent")
public class ChooseSubjectViewModel implements Serializable {

    private static final long serialVersionUID = -8778244927188371823L;

    private static final int PAGE_SIZE_DEFAULT = 15;

    private static final int ACTIVE_PAGE_DEFAULT = 0;

    private int pageSize = PAGE_SIZE_DEFAULT;
    private int activePage = ACTIVE_PAGE_DEFAULT;

    @WireVariable
    private SubjectService subjectService;

    private boolean moreResults;

    private List<SubjectListView> subjects = new ArrayList<>();

    private String keyword;

    private boolean selected = false;

    private SubjectListView subject;

    private String source;

    @Init
    public void init(@BindingParam("source") String source) {
        this.source = source;
        findSubjects();
    }

    public List<SubjectListView> getSubjects() {
        return subjects;
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

    public boolean isSelected() {
        return selected;
    }

    @Command
    @NotifyChange({ "subjects", "moreResults" })
    public void moreResults() {
        this.activePage++;
        this.findSubjects();
    }

    @Command
    @NotifyChange("selected")
    public void selectSubject(@BindingParam("subject") SubjectListView subject) {
        this.subject = subject;
        selected = true;
    }

    @Command
    public void close(@ContextParam(ContextType.VIEW) Div comp) {
        comp.detach();
        if (selected) {
            Map<String, Object> args = new HashMap<String, Object>();
            args.put("subject", subject);
            BindUtils.postGlobalCommand(null, null, "changeSubject", args);
        }
    }

    private PageResponse<SubjectListView> findSubjects() {
        PageResponse<SubjectListView> pageResponse = subjectService.findAll(activePage, pageSize, getFilters());
        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.subjects.addAll(pageResponse.getItems());
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }

        return pageResponse;
    }

    @Command
    @NotifyChange({ "subjects", "moreResults", "filterActive" })
    public void search() {
        this.setDefaultPage();
        this.subjects.clear();
        this.findSubjects();
    }

    private Map<String, String> getFilters() {
        Map<String, String> filters = new HashMap<String, String>();
        if (!StringUtils.isBlank(this.keyword)) {
            filters.put("identifier", this.keyword);
        }
        if (this.source != null) {
            filters.put("source", source);
        }
        return filters;
    }

    private void setDefaultPage() {
        this.pageSize = PAGE_SIZE_DEFAULT;
        this.activePage = ACTIVE_PAGE_DEFAULT;
    }

}
