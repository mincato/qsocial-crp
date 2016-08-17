package com.qsocialnow.common.pagination;

import java.util.List;

import com.qsocialnow.model.BackEndObject;

public class PageResponse<T> extends BackEndObject {

    private static final long serialVersionUID = 8897337859459219254L;

    private List<T> items;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalSize;

    public PageResponse() {
    }

    public PageResponse(List<T> items, Integer pageNumber, Integer pageSize, Long totalSize) {
        this.items = items;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalSize = totalSize;
    }

    public List<T> getItems() {
        return items;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Long getTotalSize() {
        return totalSize;
    }

}
