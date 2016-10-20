package com.qsocialnow.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.ActionRegistry;
import com.qsocialnow.common.model.cases.ActionRequest;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.CaseListView;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.common.model.config.User;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.persistence.ActionRegistryRepository;
import com.qsocialnow.persistence.CaseRepository;
import com.qsocialnow.persistence.TeamRepository;
import com.qsocialnow.persistence.TriggerRepository;
import com.qsocialnow.service.action.Action;

@Service
public class CaseService {

    private static final Logger log = LoggerFactory.getLogger(CaseService.class);

    @Autowired
    private CaseRepository repository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ActionRegistryRepository actionRegistryRepository;

    @Autowired
    private TriggerRepository triggerRepository;

    @Resource
    private Map<ActionType, Action> actions;

    public PageResponse<CaseListView> findAll(Integer pageNumber, Integer pageSize, String sortField, String sortOrder,
            String subject, String title, String description, String pendingResponse, String status,
            String fromOpenDate, String toOpenDate, String userName) {
        PageRequest pageRequest = new PageRequest(pageNumber, pageSize, sortField);
        pageRequest.setSortOrder(Boolean.parseBoolean(sortOrder));

        List<String> teamsToFilter = new ArrayList<String>();
        log.info("Retriving cases from :" + userName);

        List<Team> teams = teamRepository.findTeams(userName);
        if (teams != null) {
            for (Team team : teams) {
                List<User> users = team.getUsers();
                for (User user : users) {
                    if (user.getUsername().equals(userName)) {
                        if (user.isCoordinator()) {
                            log.info("User:" + userName + " coordinator: " + user.isCoordinator()
                                    + " belongs to team :" + team.getId());
                            teamsToFilter.add(team.getId());
                        }
                    }
                }
            }
        }
        log.info("After process teams - trying to retrieve cases from :" + userName);
        List<CaseListView> cases = repository.findAll(pageRequest, subject, title, description, pendingResponse,
                status, fromOpenDate, toOpenDate, teamsToFilter, userName);

        PageResponse<CaseListView> page = new PageResponse<CaseListView>(cases, pageNumber, pageSize);
        return page;
    }

    public Case findOne(String caseId) {
        try {
            Case caseObject = repository.findOne(caseId);
            return caseObject;
        } catch (Exception e) {
            log.error("There was an error executing action", e);
            throw e;
        }
    }

    public Case save(Case newCase) {
        try {
            Case caseObject = newCase;
            if (newCase.getId() == null) {
                caseObject = repository.save(newCase);
                if (newCase.getActionsRegistry() != null && newCase.getActionsRegistry().size() > 0)
                    actionRegistryRepository.create(caseObject.getId(), newCase.getActionsRegistry().get(0));
            } else {
                repository.update(newCase);
            }
            return caseObject;
        } catch (Exception e) {
            log.error("There was an error executing action", e);
            throw e;
        }
    }

    public Case executeAction(String caseId, ActionRequest actionRequest) {
        try {
            Case caseObject = repository.findOne(caseId);
            if (caseObject != null) {
                Action action = actions.get(actionRequest.getActionType());
                if (action != null) {
                    action.execute(caseObject, actionRequest.getParameters());
                    boolean updated = repository.update(caseObject);
                    if (!updated) {
                        log.error("There was an error trying to update the case");
                        throw new RuntimeException("There was an error trying to update the case");
                    }
                    ActionRegistry actionRegistry = createActionRegistry(actionRequest);
                    actionRegistryRepository.create(caseId, actionRegistry);
                } else {
                    log.warn("The action does not exist");
                    throw new RuntimeException("The action does not exist");
                }
            } else {
                log.warn("The case was not found");
                throw new RuntimeException("The case was not found");
            }
            return caseObject;
        } catch (Exception e) {
            log.error("There was an error executing action", e);
            throw e;
        }
    }

    public List<Resolution> getAvailableResolutions(String caseId) {
        List<Resolution> availableResolutions = new ArrayList<Resolution>();
        log.info("Retrieving resolution from case:" + caseId);
        Case caseObject = repository.findOne(caseId);
        if (caseObject != null) {
            log.info("Case:" + caseObject.getId());
            String triggerId = caseObject.getTriggerId();
            if (triggerId != null) {
                Trigger trigger = triggerRepository.findOne(triggerId);
                if (trigger != null) {
                    availableResolutions = trigger.getResolutions();
                }
            }
        } else {
            log.warn("The case was not found");
            throw new RuntimeException("The case was not found");
        }
        return availableResolutions;
    }

    public void setRepository(CaseRepository repository) {
        this.repository = repository;
    }

    public void setTeamRepository(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    private ActionRegistry createActionRegistry(ActionRequest actionRequest) {
        ActionRegistry actionRegistry = new ActionRegistry();
        actionRegistry.setType(actionRequest.getActionType());
        actionRegistry.setDate(new Date().getTime());
        actionRegistry.setAction(actionRequest.getActionType().name());
        if (actionRequest.getParameters() != null) {
            Object executor = actionRequest.getParameters().get(ActionParameter.EXECUTOR);
            if (executor != null) {
                actionRegistry.setUserName((String) executor);
            }
            Object comment = actionRequest.getParameters().get(ActionParameter.COMMENT);
            if (comment != null) {
                actionRegistry.setComment((String) comment);
            }
        }
        return actionRegistry;
    }

}
