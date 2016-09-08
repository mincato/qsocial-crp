package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.TriggerListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.services.TriggerService;

@VariableResolver(DelegatingVariableResolver.class)
public class TriggersViewModel implements Serializable {

	private static final long serialVersionUID = 9145343693641922196L;

	private int pageSize = 15;
	private int activePage = 0;

	private String domain;

	@WireVariable
	private TriggerService triggerService;

	private boolean moreResults;

	private List<TriggerListView> triggers = new ArrayList<>();

	private String keyword;

	@Init
	public void init(@QueryParam("domain") String domain) {
		this.domain = domain;
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

	@Command
	@NotifyChange({ "triggers", "moreResults" })
	public void moreResults() {
		this.activePage++;
		this.findTriggers(this.domain);
	}

	private PageResponse<TriggerListView> findTriggers(String domainId) {
		PageResponse<TriggerListView> pageResponse = triggerService.findAll(
				domainId, activePage, pageSize);
		if (pageResponse.getItems() != null
				&& !pageResponse.getItems().isEmpty()) {
			this.triggers.addAll(pageResponse.getItems());
			this.moreResults = true;
		} else {
			this.moreResults = false;
		}

		return pageResponse;
	}

}
