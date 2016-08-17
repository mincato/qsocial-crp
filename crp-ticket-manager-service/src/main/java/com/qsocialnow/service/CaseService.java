package com.qsocialnow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.cases.CaseListView;
import com.qsocialnow.common.pagination.PageRequest;
import com.qsocialnow.common.pagination.PageResponse;
import com.qsocialnow.persistence.CaseRepository;

@Service
public class CaseService {

    @Autowired
    private CaseRepository repository;

    public PageResponse<CaseListView> findAll(Integer pageNumber, Integer pageSize) {
        List<CaseListView> cases = repository.findAll(new PageRequest(pageNumber, pageSize));

        Long count = repository.count();

        PageResponse<CaseListView> page = new PageResponse<CaseListView>(cases, pageNumber, pageSize, count);
        return page;
    }

    public void setRepository(CaseRepository repository) {
        this.repository = repository;
    }

}
