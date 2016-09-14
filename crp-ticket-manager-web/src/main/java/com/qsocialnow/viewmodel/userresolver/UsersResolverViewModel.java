package com.qsocialnow.viewmodel.userresolver;

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

import com.qsocialnow.common.model.config.UserResolver;
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

    @Command
    public void openEdit(@BindingParam("userresolver") String userResolverId) {
        Map<String, Object> arg = new HashMap<String, Object>();
        arg.put("userresolver", userResolverId);
        Executions.createComponents("/pages/user-resolver/edit-user-resolver.zul", null, arg);
    }
    
    @Command
    public void openDelete(@BindingParam("userresolver") String userResolverId) {
        Map<String, Object> arg = new HashMap<String, Object>();
        arg.put("userresolver", userResolverId);
        Executions.createComponents("/pages/user-resolver/delete-user-resolver.zul", null, arg);
    }

    @GlobalCommand
    @NotifyChange("usersResolver")
    public void changeUserResolver(@BindingParam("userResolverChanged") UserResolver userResolverChanged) {
        if (userResolverChanged != null) {
            Optional<UserResolverListView> userResolverOptional = usersResolver.stream()
                    .filter(userResolver -> userResolver.getId().equals(userResolverChanged.getId())).findFirst();
            if (userResolverOptional.isPresent()) {
                UserResolverListView userResolverListView = userResolverOptional.get();
                userResolverListView.setSource(userResolverChanged.getSource());
                userResolverListView.setIdentifier(userResolverChanged.getIdentifier());
                userResolverListView.setActive(userResolverChanged.getActive());
            }
        }
    }
    
    @GlobalCommand
    @NotifyChange("usersResolver")
    public void deleteUserResolver(@BindingParam("userResolverDeleted") UserResolver userResolverDeleted) {
        if (userResolverDeleted != null) {
            Optional<UserResolverListView> userResolverOptional = usersResolver.stream()
                    .filter(userResolver -> userResolver.getId().equals(userResolverDeleted.getId())).findFirst();
            if (userResolverOptional.isPresent()) {
            	UserResolverListView userResolverListView = userResolverOptional.get();
                usersResolver.remove(userResolverListView );
            }
        }
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