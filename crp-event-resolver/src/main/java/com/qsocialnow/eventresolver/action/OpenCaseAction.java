package com.qsocialnow.eventresolver.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.ActionRegistry;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.Coordinates;
import com.qsocialnow.common.model.cases.Event;
import com.qsocialnow.common.model.event.InPutBeanDocument;
import com.qsocialnow.elasticsearch.configuration.Configurator;
import com.qsocialnow.elasticsearch.configuration.QueueConfigurator;
import com.qsocialnow.elasticsearch.services.cases.CaseService;
import com.qsocialnow.eventresolver.config.EventResolverConfig;
import com.qsocialnow.eventresolver.factories.BigQueueConfiguratorFactory;
import com.qsocialnow.eventresolver.factories.ElasticConfiguratorFactory;

@Component("openCaseAction")
public class OpenCaseAction implements Action<InPutBeanDocument, Case> {

    @Autowired
    private CaseService caseElasticService;

    private static final Logger log = LoggerFactory.getLogger(OpenCaseAction.class);

    @Override
    public Case execute(InPutBeanDocument inputElement, List<String> parameters) {
        log.info("Creating case...");
        Case newCase = new Case();
        newCase.setOpen(true);
        Date openDate = new Date();
        newCase.setOpenDate(openDate);
        newCase.setTitle(inputElement.getTitulo());
        Coordinates coordinates = new Coordinates();
        coordinates.setLatitude(inputElement.getLocation().getLatitud());
        coordinates.setLongitude(inputElement.getLocation().getLongitud());
        newCase.setCoordinates(coordinates);
        newCase.setCaseCategories(Arrays.asList(inputElement.getCategorias()));

        // creating first registry
        List<ActionRegistry> registries = new ArrayList<>();
        ActionRegistry registry = new ActionRegistry();
        registry.setAction("Open Case");
        registry.setAutomatic(true);
        registry.setDate(inputElement.getFechaCreacion());
        registry.setUserName(inputElement.getUsuarioCreacion());
        Event event = new Event();
        event.setId(inputElement.getId());
        event.setDescription(inputElement.getName());

        registry.setEvent(event);

        registries.add(registry);
        newCase.setActionsRegistry(registries);

        try {
            caseElasticService.indexCaseByBulkProcess(newCase);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return newCase;
    }

    @Override
    public Case execute(InPutBeanDocument inputElement, Case outputElement, List<String> parameters) {
        // TODO Auto-generated method stub
        return null;
    }

}
