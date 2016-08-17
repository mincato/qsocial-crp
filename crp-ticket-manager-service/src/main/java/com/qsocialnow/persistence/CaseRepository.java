package com.qsocialnow.persistence;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.CaseListView;
import com.qsocialnow.common.pagination.PageRequest;
import com.qsocialnow.elasticsearch.configuration.Configurator;
import com.qsocialnow.elasticsearch.services.cases.CaseService;

@Service
public class CaseRepository {

    private Logger log = LoggerFactory.getLogger(CaseRepository.class);

    @Autowired
    private CaseService caseElasticService;

    @Value("${app.elastic.cases.configurator.path}")
    private String elasticConfiguratorZnodePath;

    @Autowired
    @Qualifier("zookeeperClient")
    private CuratorFramework zookeeperClient;

    public List<CaseListView> findAll(PageRequest pageRequest) {
        List<CaseListView> cases = new ArrayList<>();

        try {
            byte[] configuratorBytes = zookeeperClient.getData().forPath(elasticConfiguratorZnodePath);
            Configurator configurator = new GsonBuilder().create().fromJson(new String(configuratorBytes),
                    Configurator.class);

            List<Case> casesRepo = caseElasticService.getCases(configurator, pageRequest.getOffset(),
                    pageRequest.getLimit());

            for (Case caseRepo : casesRepo) {
                CaseListView caseListView = new CaseListView();
                caseListView.setId(caseRepo.getId());
                caseListView.setTitle(caseRepo.getTitle());
                caseListView.setOpenDate(caseRepo.getOpenDate());
                cases.add(caseListView);
            }
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return cases;
    }

    public Long count() {
        return 50L;
    }

}
