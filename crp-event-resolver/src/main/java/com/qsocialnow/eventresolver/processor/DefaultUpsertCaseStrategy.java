package com.qsocialnow.eventresolver.processor;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.cases.ActionRegistry;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.Message;
import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.event.Event;
import com.qsocialnow.elasticsearch.services.cases.ActionRegistryService;
import com.qsocialnow.elasticsearch.services.cases.CaseService;
import com.qsocialnow.eventresolver.action.MergeCaseAction;
import com.qsocialnow.eventresolver.action.OpenCaseAction;

@Service
public class DefaultUpsertCaseStrategy implements UpsertCaseStrategy {

    private static final Logger log = LoggerFactory.getLogger(DefaultUpsertCaseStrategy.class);

    @Autowired
    private OpenCaseAction openAction;

    @Autowired
    private MergeCaseAction mergeAction;

    @Autowired
    private CaseService caseService;

    @Autowired
    private ActionRegistryService actionRegistryService;

    @Override
    public Case upsert(ExecutionMessageRequest request) {
        log.info("Processing upsert for analytic module event");
        Case caseObject = null;
        Event input = request.getInput();
        DetectionCriteria detectionCriteria = request.getDetectionCriteria();
        Case duplicatedCase = findDuplicatedCase(request);
        if (duplicatedCase != null) {
            log.info("the event is duplicated");
            Optional<Message> optionalMessage = duplicatedCase.getMessages().stream()
                    .filter(message -> message.getId().equals(input.getId())).findFirst();
            if (optionalMessage.isPresent()) {
                Message message = optionalMessage.get();
                if (message.isFromResponseDetector()) {
                    log.info("the event is duplicated. The old event is from response detector.");
                    updateEventOnRegistry(duplicatedCase, message.getId(), input);
                    message.setFromResponseDetector(false);
                    caseObject = duplicatedCase;
                }
            }
        } else {
            if (Boolean.TRUE.equals(input.getEsReproduccion())) {
                log.info("The event is a reproduction, so it is discarded");
            } else {
                if (detectionCriteria.isAlwaysOpenCase()) {
                    log.info("opening case since flag always open case is true");
                    caseObject = openAction.openCase(input, request);
                } else {
                    Case mergeCase = findCaseToMerge(request);
                    if (mergeCase != null) {
                        log.info("merging case");
                        caseObject = mergeAction.mergeCase(input, mergeCase);
                    } else {
                        log.info("opening case since there is no case to merge");
                        caseObject = openAction.openCase(input, request);
                    }
                }
            }
        }
        return caseObject;
    }

    private Case findCaseToMerge(ExecutionMessageRequest request) {
        Case caseToMerge = null;
        List<Case> openCases;
        if (request.getDetectionCriteria().isFindCaseOnAllDomains()) {
            openCases = caseService.findOpenCasesForSubject(request.getInput().getIdUsuarioCreacion());
        } else {
            openCases = caseService.findOpenCasesForSubjectByDomain(request.getInput().getIdUsuarioCreacion(), request
                    .getDomain().getId());
        }
        if (CollectionUtils.isNotEmpty(openCases)) {
            if (request.getInput().getEsSecundario()) {
                Optional<Case> optionalCase = openCases
                        .stream()
                        .filter(caseObject -> caseObject.getTriggerEvent() != null
                                && request.getInput().getIdOriginal().equals(caseObject.getTriggerEvent().getId()))
                        .findFirst();
                if (optionalCase.isPresent()) {
                    caseToMerge = optionalCase.get();
                }
            }
            if (caseToMerge == null) {
                caseToMerge = openCases.get(0);
            }
        }
        return caseToMerge;
    }

    private void updateEventOnRegistry(Case caseObject, String messageId, Event event) {
        List<ActionRegistry> actionRegistries = actionRegistryService.findRegistryByEventId(caseObject.getId(),
                messageId);
        if (CollectionUtils.isNotEmpty(actionRegistries)) {
            ActionRegistry actionRegistry = actionRegistries.get(0);
            mergeAction.updateRegistry(caseObject, actionRegistry, event);
        } else {
            log.warn(String.format("It did not found any action registry for event id %s, but I should", messageId));
        }
    }

    private Case findDuplicatedCase(ExecutionMessageRequest request) {
        Case duplicatedCase = null;
        List<Case> casesFounded = caseService.findCasesByMessageIdByDomain(request.getInput().getId(), request
                .getDomain().getId());
        if (CollectionUtils.isNotEmpty(casesFounded)) {
            duplicatedCase = casesFounded.get(0);
        }
        return duplicatedCase;
    }

}
