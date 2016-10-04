package com.qsocialnow.eventresolver.action;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.event.InPutBeanDocument;
import com.qsocialnow.elasticsearch.services.cases.CaseService;
import com.qsocialnow.elasticsearch.services.cases.SubjectService;
import com.qsocialnow.eventresolver.processor.ExecutionMessageRequest;

@Component("openCaseAction")
public class OpenCaseAction implements Action<InPutBeanDocument, Case> {

    @Autowired
    private CaseService caseElasticService;

    @Autowired
    private SubjectService subjectService;

    private static final Logger log = LoggerFactory.getLogger(OpenCaseAction.class);

    @Override
    public Case execute(InPutBeanDocument inputElement, List<String> parameters, ExecutionMessageRequest request) {
        log.info("Executing action: " + ActionType.OPEN_CASE.name());

        String sourceId = inputElement.getIdUsuarioOriginal();
        Case newCase = Case.getNewCaseFromEvent(inputElement);
        try {
            Subject subject = findSubject(sourceId);
            if (subject == null) {
                subject = new Subject();

                log.info("Creating subject: " + sourceId + " identifier:" + inputElement.getUsuarioOriginal()
                        + " source:" + inputElement.getMedioId());

                subject.setLastAccionDate(new Date()); //
                subject.setSignedDate(new Date());
                subject.setProfileImage(inputElement.getProfileImage());
                subject.setIdentifier(inputElement.getUsuarioOriginal());
                subject.setSourceId(sourceId);

                subject.setSource(inputElement.getMedioId());

                String idSubject = subjectService.indexSubject(subject);
                subject.setId(idSubject);
            }

            newCase.setDomainId(request.getDomain().getId());
            newCase.setTriggerId(request.getTrigger().getId());
            newCase.setSegmentId(request.getSegment().getId());
            newCase.setSubject(subject);

            caseElasticService.indexCaseByBulkProcess(newCase);

        } catch (Exception e) {
            log.error("There was an error executing action", e);
        }
        return newCase;
    }

    private Subject findSubject(String idOriginUser) {
        log.info("Retrieving subject: " + idOriginUser);
        Subject subject = subjectService.findSubjectsByOriginUser(idOriginUser);
        return subject;
    }

    @Override
    public Case execute(InPutBeanDocument inputElement, Case outputElement, List<String> parameters) {
        // TODO Auto-generated method stub
        return null;
    }

}
