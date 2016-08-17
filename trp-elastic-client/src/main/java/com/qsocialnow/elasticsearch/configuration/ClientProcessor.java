package com.qsocialnow.elasticsearch.configuration;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.Client;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.elasticsearch.repositories.ConfigurationRepository;
import com.qsocialnow.elasticsearch.services.ConfigurationService;
import com.qsocialnow.elasticsearch.services.DetectionCriteriaService;
import com.qsocialnow.elasticsearch.services.DomainService;
import com.qsocialnow.elasticsearch.services.cases.CaseService;

public class ClientProcessor {

    private static final Logger log = LoggerFactory.getLogger(Client.class);

    public ClientProcessor() {

    }

    public static void executeCommand(final String[] commands) {

        DomainService service = new DomainService();
        ConfigurationService configService = new ConfigurationService();

        CaseService caseService = new CaseService();
        ConfigurationRepository repo = new ConfigurationRepository();

        Configurator configurator = new Configurator();

        DetectionCriteriaService criteriaService = new DetectionCriteriaService();
        Domain domain = repo.findDomainByName("domain1");
        Case caseDocument = new Case();
        caseDocument.setDescription("Adding new case");

        switch (commands[0]) {
            case "createindex":
                configService.createIndex("configuration");
                break;

            case "rmindex":

                if (commands.length > 1) {

                    switch (commands[1]) {
                        case "domain":
                            configService.deleteIndex("configuration");
                            break;

                        case "case":
                            configService.deleteCaseIndex(commands[2]);
                            break;
                    }
                } else {
                    log.error("Invalid command ,allowed parameters: domain , criteria");
                }
                break;

            case "add":

                if (commands.length > 1) {
                    String response;
                    switch (commands[1]) {
                        case "domain":
                            // response = service.indexDomain(domain);
                            // log.info("Index Response:ID=" + response);
                            break;
                        case "criteria":
                            response = criteriaService.indexDetectionCriteria(domain.getTriggers().get(0).getSegments()
                                    .get(0).getDetectionCriterias().get(0));
                            log.info("Index Response:ID=" + response);
                            break;

                        case "case":
                            response = caseService.indexCase(caseDocument);
                            log.info("Index Response:ID=" + response);
                            break;
                    }
                } else {
                    log.error("Invalid command ,allowed parameters: domain , criteria");
                }
                break;

            case "search":
                // Domain document = service.findDomainByName(configurator,
                // name)findDomainByName("domain1");
                // log.info("Search domain: " + document.getName() + "- id:" +
                // document.getId());
                break;

            case "searchcases":
                List<Case> cases = caseService.getCases(Integer.valueOf(commands[1]), Integer.valueOf(commands[2]));
                for (Case case1 : cases) {
                    log.info("Search case: " + case1.getOpenDate() + "- id:" + case1.getId());
                }
                break;

            default:
                break;
        }
    }

}
