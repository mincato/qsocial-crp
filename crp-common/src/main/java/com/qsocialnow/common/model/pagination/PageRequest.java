package com.qsocialnow.common.model.pagination;

import java.io.Serializable;

public class PageRequest implements Serializable {

    private static final long serialVersionUID = -7328689392321734924L;

    private final Integer pageNumber;
    private final Integer pageSize;
    private final Integer limit;
    private final Integer offset;
    private final String sortField;

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 20;

    public PageRequest(final Integer pageNumber, final Integer pageSize,final String sortField) {
        this.pageNumber = (pageNumber == null) ? DEFAULT_PAGE_NUMBER : pageNumber;
        this.pageSize = (pageSize == null) ? DEFAULT_PAGE_SIZE : pageSize;
        this.limit = this.pageSize;
        this.offset = this.pageSize * this.pageNumber;
        this.sortField = sortField;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getOffset() {
        return offset;
    }

	public String getSortField() {
		return sortField;
	}
}
