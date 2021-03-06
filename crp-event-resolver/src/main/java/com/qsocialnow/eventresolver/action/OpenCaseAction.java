package com.qsocialnow.eventresolver.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.Person;
import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.config.BaseUser;
import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.common.model.config.User;
import com.qsocialnow.common.model.event.Event;
import com.qsocialnow.common.services.SourceService;
import com.qsocialnow.common.services.strategies.SourceStrategy;
import com.qsocialnow.elasticsearch.services.cases.PersonService;
import com.qsocialnow.elasticsearch.services.cases.SubjectService;
import com.qsocialnow.eventresolver.processor.ExecutionMessageRequest;
import com.qsocialnow.eventresolver.service.TeamService;

@Component("openCaseAction")
public class OpenCaseAction {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private PersonService personService;

    @Autowired
    private SourceService sourceService;

    @Autowired
    private TeamService teamService;

    @Resource
    private Map<Long, SourceStrategy> sources;

    private Random random = new Random();

    private static final Logger log = LoggerFactory.getLogger(OpenCaseAction.class);

    public Case openCase(Event inputElement, ExecutionMessageRequest request) {
        log.debug("Executing action: " + ActionType.OPEN_CASE.name());
        String sourceId = inputElement.getIdUsuarioCreacion();
        Case newCase = Case.getNewCaseFromEvent(inputElement, sourceService);
        try {
            Subject subject = findSubject(sourceId);
            if (subject == null) {
                subject = new Subject();

                log.debug("Creating subject: " + sourceId + " identifier:" + inputElement.getUsuarioCreacion()
                        + " source:" + inputElement.getMedioId());

                String personId = createPerson(inputElement);

                subject.setPersonId(personId);
                subject.setLastAccionDate(new Date()); //
                subject.setSignedDate(new Date());
                subject.setIdentifier(inputElement.getUsuarioCreacion());
                subject.setSourceId(sourceId);
                if (sources.get(inputElement.getMedioId()) != null) {
                    String originalSourceId = sources.get(inputElement.getMedioId()).getOriginalSourceId(inputElement);
                    subject.setOriginalSourceId(originalSourceId != null ? originalSourceId : sourceId);
                }
                subject.setProfileImage(inputElement.getProfileImage());
                subject.setSourceName(inputElement.getName() != null ? inputElement.getName() : subject.getIdentifier());
                subject.setLocationMethod(inputElement.getLocationMethod());
                subject.setLocation(inputElement.getOriginalLocation());

                subject.setSource(inputElement.getMedioId());

                String idSubject = subjectService.indexSubject(subject);
                subject.setId(idSubject);
            }

            newCase.setDomainId(request.getDomain().getId());
            newCase.setTriggerId(request.getTrigger().getId());
            newCase.setSegmentId(request.getSegment().getId());

            if (request.getSegment() != null) {
                newCase.setTeamId(request.getSegment().getTeam());
                newCase.setAssignee(getRandomUser(newCase.getTeamId()));
            }

            newCase.setSubject(subject);
            newCase.setTriggerEvent(inputElement);

        } catch (Exception e) {
            log.error("There was an error executing action", e);
        }
        return newCase;
    }

    private String createPerson(Event inputElement) {
        Person person = new Person();
        return personService.indexPerson(person);
    }

    private BaseUser getRandomUser(String teamId) {
        if (teamId != null) {
            Team team = teamService.findTeam(teamId);
            List<User> users = team.getUsers();
            if (CollectionUtils.isNotEmpty(users)) {
                int index = random.nextInt(users.size());
                return new BaseUser(users.get(index));
            }
        }
        return null;
    }

    private Subject findSubject(String idOriginUser) {
        log.debug("Retrieving subject: " + idOriginUser);
        Subject subject = subjectService.findSubjectsByOriginUser(idOriginUser);
        return subject;
    }

}
