package com.qsocialnow.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.ActionRegistry;
import com.qsocialnow.common.model.cases.ActionRegistryStatus;
import com.qsocialnow.common.model.cases.ActionRequest;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.CaseListView;
import com.qsocialnow.common.model.cases.CasesFilterRequest;
import com.qsocialnow.common.model.cases.Person;
import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.Media;
import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.common.model.config.User;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.elasticsearch.services.cases.PersonService;
import com.qsocialnow.persistence.ActionRegistryRepository;
import com.qsocialnow.persistence.CaseRepository;
import com.qsocialnow.persistence.DomainRepository;
import com.qsocialnow.persistence.TeamRepository;
import com.qsocialnow.persistence.TriggerRepository;
import com.qsocialnow.service.action.Action;
import com.qsocialnow.service.action.AsyncAction;

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

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private PersonService personService;

    @Resource
    private Map<ActionType, Action> actions;

    public PageResponse<CaseListView> findAllByFilters(CasesFilterRequest filterRequest) {
        List<String> teamsToFilter = new ArrayList<String>();
        List<Team> teams = teamRepository.findTeams(filterRequest.getUserName());
        if (teams != null) {
            for (Team team : teams) {
                List<User> users = team.getUsers();
                for (User user : users) {
                    if (user.getUsername().equals(filterRequest.getUserName())) {
                        if (user.isCoordinator()) {
                            teamsToFilter.add(team.getId());
                        }
                    }
                }
            }
        }
        filterRequest.setTeamsToFilter(teamsToFilter);
        List<CaseListView> cases = repository.findAll(filterRequest);
        PageResponse<CaseListView> page = new PageResponse<CaseListView>(cases, filterRequest.getPageRequest()
                .getPageNumber(), filterRequest.getPageRequest().getPageSize());
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
                if (Media.MANUAL.getValue().equals(newCase.getSource()) && newCase.getSubject().getId() == null) {
                    Subject subject = initSubject(newCase);
                    newCase.setSubject(subjectService.createSubject(subject));
                }
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

    public Subject initSubject(Case newCase) {
        Person person = new Person();
        String personId = personService.indexPerson(person);
        Subject subject = newCase.getSubject();
        subject.setPersonId(personId);
        return subject;
    }

    public Case executeAction(String caseId, ActionRequest actionRequest) {
        try {
            Case caseObject = repository.findOne(caseId);
            if (caseObject != null) {
                Action action = actions.get(actionRequest.getActionType());
                if (action != null) {
                    AsyncAction asyncAction = action.execute(caseObject, actionRequest.getParameters());
                    boolean updated = repository.update(caseObject);
                    if (!updated) {
                        log.error("There was an error trying to update the case");
                        throw new RuntimeException("There was an error trying to update the case");
                    }
                    ActionRegistry actionRegistry = createActionRegistry(actionRequest);
                    actionRegistryRepository.create(caseId, actionRegistry);
                    if (asyncAction != null) {
                        asyncAction.postProcess(caseObject, actionRequest.getParameters(), actionRegistry);
                    }
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
                    String domainId = caseObject.getDomainId();
                    Domain domain = domainRepository.findOneWithActiveResolutions(domainId);
                    availableResolutions = filterActiveResolutions(trigger.getResolutions(), domain.getResolutions());
                }
            }
        } else {
            log.warn("The case was not found");
            throw new RuntimeException("The case was not found");
        }
        return availableResolutions;
    }

    private List<Resolution> filterActiveResolutions(List<Resolution> triggerResolutions,
            List<Resolution> domainResolutions) {
        return triggerResolutions
                .stream()
                .filter(triggerRes -> {
                    String resolutionId = triggerRes.getId();
                    Optional<Resolution> domainRes = domainResolutions.stream()
                            .filter(res -> res.getId().equals(resolutionId)).findFirst();
                    return domainRes.isPresent();
                }).collect(Collectors.toList());
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
            Object status = actionRequest.getParameters().get(ActionParameter.ACTION_REGISTRY_STATUS);
            if (status != null) {
                actionRegistry.setStatus((ActionRegistryStatus) status);
            }
        }
        return actionRegistry;
    }

}
