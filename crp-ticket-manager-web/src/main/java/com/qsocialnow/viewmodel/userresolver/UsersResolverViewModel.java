package com.qsocialnow.viewmodel.userresolver;

import static com.qsocialnow.pagination.PaginationConstants.ACTIVE_PAGE_DEFAULT;
import static com.qsocialnow.pagination.PaginationConstants.PAGE_SIZE_DEFAULT;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.UserResolverListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.services.UserResolverService;

@VariableResolver(DelegatingVariableResolver.class)
public class UsersResolverViewModel implements Serializable {

    private static final long serialVersionUID = -6677122190515536127L;

    private int pageSize = PAGE_SIZE_DEFAULT;
    private int activePage = ACTIVE_PAGE_DEFAULT;

    @WireVariable("mockUserResolverService")
    private UserResolverService userResolverService;

    private boolean moreResults;

    private List<UserResolverListView> usersResolver = new ArrayList<>();

    private String keyword;

    private boolean filterActive = false;

    @Init
    public void init() {
        findUsersResolver();
    }

    public List<UserResolverListView> getUserResolvers() {
        return this.usersResolver;
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
    @NotifyChange({ "usersResolver", "moreResults" })
    public void moreResults() {
        this.activePage++;
        this.findUsersResolver();
    }

    private PageResponse<UserResolverListView> findUsersResolver() {
        PageResponse<UserResolverListView> pageResponse = userResolverService.findAll(activePage, pageSize,
                getFilters());
        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.usersResolver.addAll(pageResponse.getItems());
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }

        return pageResponse;
    }

    @Command
    @NotifyChange({ "usersResolver", "moreResults", "filterActive" })
    public void search() {
        this.filterActive = !StringUtils.isEmpty(this.keyword);
        this.setDefaultPage();
        this.usersResolver.clear();
        this.findUsersResolver();
    }

    private Map<String, String> getFilters() {
        if (this.keyword == null || this.keyword.isEmpty() || !filterActive) {
            return null;
        }
        Map<String, String> filters = new HashMap<String, String>();
        return filters;
    }

    private void setDefaultPage() {
        this.pageSize = PAGE_SIZE_DEFAULT;
        this.activePage = ACTIVE_PAGE_DEFAULT;
    }

    public boolean isFilterActive() {
        return filterActive;
    }

    public List<UserResolverListView> getUsersResolver() {
        return usersResolver;
    }

    public void setUsersResolver(List<UserResolverListView> usersResolver) {
        this.usersResolver = usersResolver;
    }

}
