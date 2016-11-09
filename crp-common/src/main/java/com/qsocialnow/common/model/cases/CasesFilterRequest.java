package com.qsocialnow.common.model.cases;

import java.util.List;

import com.qsocialnow.common.model.config.Category;
import com.qsocialnow.common.model.filter.AdministrativeUnitsFilter;
import com.qsocialnow.common.model.filter.RangeRequest;
import com.qsocialnow.common.model.filter.WordsFilterRequestBean;
import com.qsocialnow.common.model.pagination.PageRequest;

public class CasesFilterRequest {

	private boolean filterActive;
	
	private PageRequest pageRequest;
	
	private String domain;
	
	private String trigger;
	
	private String segment;	
	
	private Long fromDate;

    private Long toDate;

    private String title;

    private String description;

    private String subject;

    private String userName;
    
    private List<String> teamsToFilter;

    private boolean isAdmin;
    
    private String status;

    private String priority;

    private String pendingResponse;
    
    private String caseCategory;
    
    private String subjectCategory;
	
	private Long tokenId;

    private String serieName;

    private Long serieId;

    private Long subSerieId;

    private String subSerieName;

    private Long[] subSeriesDefined;

    private Long timeFrom;

    private Long timeTo;

    private String[] languages;

    private Integer[] mediums;

    private Integer mediumType;

    private String[] connotations;

    private Long[] categories;

    private RangeRequest range;

    private AdministrativeUnitsFilter administrativeUnitsFilter;

    private WordsFilterRequestBean cloudsurfer;

    private String timezone;

    private String language;

    private Long createdDate;

    private String username;

    private List<Category> categoriesFilter;

    public PageRequest getPageRequest() {
		return pageRequest;
	}

	public void setPageRequest(PageRequest pageRequest) {
		this.pageRequest = pageRequest;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}

	public boolean isFilterActive() {
		return filterActive;
	}

	public void setFilterActive(boolean filterActive) {
		this.filterActive = filterActive;
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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<String> getTeamsToFilter() {
		return teamsToFilter;
	}

	public void setTeamsToFilter(List<String> teamsToFilter) {
		this.teamsToFilter = teamsToFilter;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getPendingResponse() {
		return pendingResponse;
	}

	public void setPendingResponse(String pendingResponse) {
		this.pendingResponse = pendingResponse;
	}

	public String getCaseCategory() {
		return caseCategory;
	}

	public void setCaseCategory(String caseCategory) {
		this.caseCategory = caseCategory;
	}

	public String getSubjectCategory() {
		return subjectCategory;
	}

	public void setSubjectCategory(String subjectCategory) {
		this.subjectCategory = subjectCategory;
	}

	public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public String getSerieName() {
        return serieName;
    }

    public void setSerieName(String serieName) {
        this.serieName = serieName;
    }

    public Long getSerieId() {
        return serieId;
    }

    public void setSerieId(Long serieId) {
        this.serieId = serieId;
    }

    public Long getSubSerieId() {
        return subSerieId;
    }

    public void setSubSerieId(Long subSerieId) {
        this.subSerieId = subSerieId;
    }

    public Long[] getSubSeriesDefined() {
        return subSeriesDefined;
    }

    public void setSubSeriesDefined(Long[] subSeriesDefined) {
        this.subSeriesDefined = subSeriesDefined;
    }

    public Long getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(Long timeFrom) {
        this.timeFrom = timeFrom;
    }

    public Long getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(Long timeTo) {
        this.timeTo = timeTo;
    }

    public String[] getLanguages() {
        return languages;
    }

    public void setLanguages(String[] languages) {
        this.languages = languages;
    }

    public Integer[] getMediums() {
        return mediums;
    }

    public void setMediums(Integer[] mediums) {
        this.mediums = mediums;
    }

    public Integer getMediumType() {
        return mediumType;
    }

    public void setMediumType(Integer mediumType) {
        this.mediumType = mediumType;
    }

    public String[] getConnotations() {
        return connotations;
    }

    public void setConnotations(String[] connotations) {
        this.connotations = connotations;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Long[] getCategories() {
        return categories;
    }

    public void setCategories(Long[] categories) {
        this.categories = categories;
    }

    public RangeRequest getRange() {
        return range;
    }

    public void setRange(RangeRequest range) {
        this.range = range;
    }

    public AdministrativeUnitsFilter getAdministrativeUnitsFilter() {
        return administrativeUnitsFilter;
    }

    public void setAdministrativeUnitsFilter(AdministrativeUnitsFilter administrativeUnitsFilter) {
        this.administrativeUnitsFilter = administrativeUnitsFilter;
    }

    public WordsFilterRequestBean getCloudsurfer() {
        return cloudsurfer;
    }

    public void setCloudsurfer(WordsFilterRequestBean cloudsurfer) {
        this.cloudsurfer = cloudsurfer;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getSubSerieName() {
        return subSerieName;
    }

    public void setSubSerieName(String subSerieName) {
        this.subSerieName = subSerieName;
    }

    public List<Category> getCategoriesFilter() {
        return categoriesFilter;
    }

    public void setCategoriesFilter(List<Category> categoriesFilter) {
        this.categoriesFilter = categoriesFilter;
    }

}
