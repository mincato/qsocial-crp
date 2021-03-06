package com.qsocialnow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.cases.RegistryListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.persistence.ActionRegistryRepository;

@Service
public class ActionRegistryService {

    @Autowired
    private ActionRegistryRepository repository;

    public PageResponse<RegistryListView> findAll(String caseId, Integer pageNumber, Integer pageSize) {
        List<RegistryListView> cases = repository.findAll(caseId, new PageRequest(pageNumber, pageSize, null));

        PageResponse<RegistryListView> page = new PageResponse<RegistryListView>(cases, pageNumber, pageSize);
        return page;
    }

    public PageResponse<RegistryListView> findAllBy(String caseId, String textValue, String action, String user,
            String fromDate, String toDate, Integer pageNumber, Integer pageSize) {

        List<RegistryListView> cases = repository.findAllBy(caseId, textValue, action, user, fromDate, toDate,
                new PageRequest(pageNumber, pageSize, null));

        PageResponse<RegistryListView> page = new PageResponse<RegistryListView>(cases, pageNumber, pageSize);
        return page;
    }

    public void setRepository(ActionRegistryRepository repository) {
        this.repository = repository;
    }

}
