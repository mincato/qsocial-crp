package com.qsocialnow.eventresolver.action;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qsocial.eventresolver.model.event.InPutBeanDocument;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.Coordinates;
import com.qsocialnow.elasticsearch.configuration.Configurator;
import com.qsocialnow.elasticsearch.services.cases.CaseService;
import com.qsocialnow.eventresolver.config.EventResolverConfig;
import com.qsocialnow.eventresolver.factories.ElasticConfiguratorFactory;

@Component("openCaseAction")
public class OpenCaseAction implements Action<InPutBeanDocument, Case> {

    @Autowired
    private EventResolverConfig appConfig;

    @Autowired
    private ElasticConfiguratorFactory elasticConfigConfiguratorFactory;

    @Autowired
    private CaseService caseElasticService;

    private static final Logger log = LoggerFactory.getLogger(OpenCaseAction.class);

    @Override
    public Case execute(InPutBeanDocument inputElement, List<String> parameters) {
        log.info("Creating case...");
        Case newCase = new Case();
        newCase.setOpen(true);
        newCase.setOpenDate(new Date());
        newCase.setTitle(inputElement.getTitulo());
        Coordinates coordinates = new Coordinates();
        coordinates.setLatitude(inputElement.getLocation().getLatitud());
        coordinates.setLongitude(inputElement.getLocation().getLongitud());
        newCase.setCoordinates(coordinates);
        newCase.setCaseCategories(Arrays.asList(inputElement.getCategorias()));

        Configurator elasticConfigConfigurator;
        try {
            elasticConfigConfigurator = elasticConfigConfiguratorFactory.getConfigurator(appConfig
                    .getElasticCasesConfiguratorZnodePath());

            caseElasticService.indexCase(elasticConfigConfigurator, newCase);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return newCase;
    }

}
