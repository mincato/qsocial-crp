package com.qsocialnow.viewmodel.team;

import static com.qsocialnow.pagination.PaginationConstants.ACTIVE_PAGE_DEFAULT;
import static com.qsocialnow.pagination.PaginationConstants.PAGE_SIZE_DEFAULT;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.common.model.config.TeamListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.services.TeamService;

@VariableResolver(DelegatingVariableResolver.class)
public class TeamsViewModel implements Serializable {

    private static final long serialVersionUID = -759909708888032625L;

    private int pageSize = PAGE_SIZE_DEFAULT;
    private int activePage = ACTIVE_PAGE_DEFAULT;

    @WireVariable("mockTeamService")
    private TeamService teamService;

    private boolean moreResults;

    private List<TeamListView> teams = new ArrayList<TeamListView>();

    private String keyword;

    private boolean filterActive = false;

    @Init
    public void init() {
        findTeams();
    }

    public List<TeamListView> getTeams() {
        return this.teams;
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

    @Command
    @NotifyChange({ "teams", "moreResults" })
    public void moreResults() {
        this.activePage++;
        this.findTeams();
    }

    private PageResponse<TeamListView> findTeams() {
        PageResponse<TeamListView> pageResponse = teamService.findAll(activePage, pageSize, getFilters());
        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.teams.addAll(pageResponse.getItems());
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }

        return pageResponse;
    }

    @Command
    @NotifyChange({ "teams", "moreResults", "filterActive" })
    public void search() {
        this.filterActive = !StringUtils.isEmpty(this.keyword);
        this.setDefaultPage();
        this.teams.clear();
        this.findTeams();
    }

    private Map<String, String> getFilters() {
        if (this.keyword == null || this.keyword.isEmpty() || !filterActive) {
            return null;
        }
        Map<String, String> filters = new HashMap<String, String>();
        filters.put("name", this.keyword);
        return filters;
    }

    private void setDefaultPage() {
        this.pageSize = PAGE_SIZE_DEFAULT;
        this.activePage = ACTIVE_PAGE_DEFAULT;
    }

    public boolean isFilterActive() {
        return filterActive;
    }

    @Command
    public void openEdit(@BindingParam("teamId") String teamId) {
        Map<String, Object> arg = new HashMap<String, Object>();
        arg.put("team", teamId);
        Executions.createComponents("/pages/team/edit-team.zul", null, arg);
    }

    @GlobalCommand
    @NotifyChange("teams")
    public void changeTeam(@BindingParam("teamChanged") Team teamChanged) {
        if (teamChanged != null) {
            Optional<TeamListView> teamOptional = teams.stream()
                    .filter(team -> team.getId().equals(teamChanged.getId())).findFirst();
            if (teamOptional.isPresent()) {
                TeamListView teamListView = teamOptional.get();
                teamListView.setName(teamChanged.getName());
            }
        }
    }

}