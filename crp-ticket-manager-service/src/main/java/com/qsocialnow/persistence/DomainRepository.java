package com.qsocialnow.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.DomainListView;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.elasticsearch.services.config.DomainService;

@Service
public class DomainRepository {

    private Logger log = LoggerFactory.getLogger(DomainRepository.class);

    @Autowired
    private DomainService domainElasticService;

    public Domain save(Domain newDomain) {
        try {
            String id = domainElasticService.indexDomain(newDomain);
            newDomain.setId(id);

            return newDomain;
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

    public Domain update(Domain domain) {
        try {
            String id = domainElasticService.updateDomain(domain);
            domain.setId(id);
            return domain;
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

    public Domain findOne(String domainId) {
        try {
            Domain domain = domainElasticService.findDomainWithResolutions(domainId);

            return domain;
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

    public List<DomainListView> findAll(PageRequest pageRequest) {
        List<DomainListView> domains = new ArrayList<>();

        try {
            List<Domain> domainsRepo = domainElasticService.getDomains(pageRequest.getOffset(), pageRequest.getLimit());

            for (Domain domainRepo : domainsRepo) {
                DomainListView domainListView = new DomainListView();
                domainListView.setId(domainRepo.getId());
                domainListView.setName(domainRepo.getName());

                List<Long> thematics = domainRepo.getThematics();
                domainListView.setThematicIds(thematics);
                domains.add(domainListView);
            }
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return domains;
    }

    public List<DomainListView> findAllByName(PageRequest pageRequest, String name) {
        List<DomainListView> domains = new ArrayList<>();

        try {
            List<Domain> domainsRepo = domainElasticService.getDomainsByName(pageRequest.getOffset(),
                    pageRequest.getLimit(), name);

            for (Domain domainRepo : domainsRepo) {
                DomainListView domainListView = new DomainListView();
                domainListView.setId(domainRepo.getId());
                domainListView.setName(domainRepo.getName());

                List<Long> thematics = domainRepo.getThematics();
                domainListView.setThematicIds(thematics);
                domains.add(domainListView);
            }
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return domains;
    }

    public Map<String, String> findAllReport() {
        return domainElasticService.getAllDomainsAsMap();
    }

}