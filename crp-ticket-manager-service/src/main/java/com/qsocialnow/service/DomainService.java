package com.qsocialnow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.persistence.DomainRepository;

@Service
public class DomainService {

    @Autowired
    private DomainRepository repository;

    @Autowired
    private TopicService topicService;

    public Domain save(Domain newDomain) {
        Domain domainSaved = repository.save(newDomain);
        topicService.create(newDomain.getName());
        return domainSaved;
    }

    public void setRepository(DomainRepository repository) {
        this.repository = repository;
    }

}
