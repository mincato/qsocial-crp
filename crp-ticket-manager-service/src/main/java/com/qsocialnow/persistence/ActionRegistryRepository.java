package com.qsocialnow.persistence;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.cases.ActionRegistry;
import com.qsocialnow.common.model.cases.RegistryListView;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.elasticsearch.services.cases.ActionRegistryService;

@Component
public class ActionRegistryRepository {

    private static final Logger log = LoggerFactory.getLogger(ActionRegistryRepository.class);

    @Autowired
    private ActionRegistryService registryService;

    public void create(String caseId, ActionRegistry actionRegistry) {
        String id = registryService.indexRegistry(caseId, actionRegistry);
        actionRegistry.setId(id);
        log.info("Saving action registry: " + new GsonBuilder().create().toJson(actionRegistry));
    }

    public List<RegistryListView> findAll(String caseId, PageRequest pageRequest) {
        List<RegistryListView> registriesView = new ArrayList<>();

        try {

            List<ActionRegistry> registries = registryService.findRegistries(pageRequest.getOffset(),
                    pageRequest.getLimit(), caseId);
            if (registries != null) {
                for (ActionRegistry registry : registries) {
                    RegistryListView registryListView = new RegistryListView();
                    registryListView.setId(registry.getId());
                    registryListView.setUser(registry.getUserName());
                    registryListView.setAction(registry.getAction());
                    registryListView.setAutomatic(registry.getAutomatic());
                    /*
                     * if (registry.getEvent() != null)
                     * registryListView.setDescription
                     * (registry.getEvent().getDescription());
                     */
                    registryListView.setDescription(registry.getComment());
                    registryListView.setDate(registry.getDate());
                    registryListView.setDeepLink(registry.getDeepLink());
                    registryListView.setStatus(registry.getStatus());
                    registryListView.setErrorType(registry.getErrorType());
                    registriesView.add(registryListView);
                }
            }
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return registriesView;
    }

    public List<RegistryListView> findAllBy(String caseId, String textValue, String action, String user,
            String fromDate, String toDate, PageRequest pageRequest) {
        List<RegistryListView> registriesView = new ArrayList<>();
        try {
            List<ActionRegistry> registries = registryService.findRegistriesBy(pageRequest.getOffset(),
                    pageRequest.getLimit(), caseId, textValue, action, user, fromDate, toDate);
            if (registries != null) {
                for (ActionRegistry registry : registries) {
                    RegistryListView registryListView = new RegistryListView();
                    registryListView.setId(registry.getId());
                    registryListView.setUser(registry.getUserName());
                    registryListView.setAction(registry.getAction());
                    registryListView.setAutomatic(registry.getAutomatic());
                    /*
                     * if (registry.getEvent() != null)
                     * registryListView.setDescription
                     * (registry.getEvent().getDescription());
                     */
                    registryListView.setDescription(registry.getComment());
                    registryListView.setDate(registry.getDate());
                    registryListView.setDeepLink(registry.getDeepLink());
                    registryListView.setStatus(registry.getStatus());
                    registryListView.setErrorType(registry.getErrorType());
                    registriesView.add(registryListView);
                }
            }
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return registriesView;
    }

    public ActionRegistry findOne(String actionRegistryId) {
        return registryService.findOne(actionRegistryId);
    }

    public void update(String caseId, ActionRegistry actionRegistry) {
        registryService.update(caseId, actionRegistry);

    }

}
