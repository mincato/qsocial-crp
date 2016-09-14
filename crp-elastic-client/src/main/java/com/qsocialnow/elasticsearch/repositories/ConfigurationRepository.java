package com.qsocialnow.elasticsearch.repositories;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.config.AutomaticActionCriteria;
import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.common.model.config.LanguageFilter;
import com.qsocialnow.common.model.config.MediaFilter;
import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.common.model.config.Segment;
import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.common.model.config.User;
import com.qsocialnow.common.model.config.UserResolver;

public class ConfigurationRepository {

    public Domain findDomainByName(String name) {

        Domain domain = new Domain();
        domain.setId("6a8ca01c-7896-48e9-81cc-9f70661fcb32");
        domain.setName("domain1");
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            String initDateString = "31-12-2015 09:00:00";
            String endDateString = "31-12-2017 09:00:00";

            Date initDate = sdf.parse(initDateString);
            Date endDate = sdf.parse(endDateString);

            Trigger trigger1 = new Trigger();
            trigger1.setId(name + "-trigger 1");
            trigger1.setInit(initDate);
            trigger1.setEnd(endDate);
            trigger1.setDescription("Attend complains about new campaigns");
            trigger1.setSegments(getSegmentsByTriggerId(trigger1.getId()));
            trigger1.setResolutions(getResolutionsByTriggerId(trigger1.getId()));

            Trigger trigger2 = new Trigger();
            trigger2.setId(name + "-trigger 2");
            trigger2.setInit(initDate);
            trigger2.setEnd(endDate);
            trigger2.setDescription("Send new information about prices increase");
            trigger2.setSegments(getSegmentsByTriggerId(trigger2.getId()));
            trigger2.setResolutions(getResolutionsByTriggerId(trigger2.getId()));

            Trigger trigger3 = new Trigger();
            trigger3.setId(name + "-trigger 3");
            trigger3.setInit(initDate);
            trigger3.setEnd(endDate);
            trigger3.setDescription("Review marketing campaing");
            trigger3.setSegments(getSegmentsByTriggerId(trigger3.getId()));
            trigger3.setResolutions(getResolutionsByTriggerId(trigger3.getId()));

            List<Trigger> triggers = new ArrayList<>();
            triggers.add(trigger1);
            triggers.add(trigger2);
            triggers.add(trigger3);

            domain.setTriggers(triggers);

        } catch (ParseException e) {

        }
        return domain;
    }

    private List<Resolution> getResolutionsByTriggerId(String id) {
        List<Resolution> resolutions = new ArrayList<>();

        Resolution resolution1 = new Resolution();
        resolution1.setDescription("Sending email to subject");
        resolution1.setId(id + " - resolution 1");

        Resolution resolution2 = new Resolution();
        resolution2.setDescription("Posting twitter nameing subject");
        resolution2.setId(id + " - resolution 2");

        resolutions.add(resolution1);
        resolutions.add(resolution2);

        return resolutions;
    }

    private List<Segment> getSegmentsByTriggerId(String id) throws ParseException {

        List<Segment> segmentsFromTrigger = new ArrayList<>();
        Segment segment1 = new Segment();
        segment1.setId(id + "-segment 1");
        segment1.setDescription(id + " segment 1- description");
        segment1.setDetectionCriterias(getDetectionCriteriaBySegmentId(segment1.getId()));
        segment1.setTeam(getTeamBySegmentId(segment1.getId()));

        Segment segment2 = new Segment();
        segment2.setId(id + "-segment 2");
        segment2.setDescription(id + " segment 2- description");
        segment2.setDetectionCriterias(getDetectionCriteriaBySegmentId(segment2.getId()));
        segment2.setTeam(getTeamBySegmentId(segment2.getId()));

        Segment segment3 = new Segment();
        segment3.setId(id + "-segment 3");
        segment3.setDescription(id + " segment 3- description");
        segment3.setDetectionCriterias(getDetectionCriteriaBySegmentId(segment3.getId()));
        segment3.setTeam(getTeamBySegmentId(segment3.getId()));

        segmentsFromTrigger.add(segment1);
        segmentsFromTrigger.add(segment2);
        segmentsFromTrigger.add(segment3);

        return segmentsFromTrigger;
    }

    private Team getTeamBySegmentId(String id) {
        Team team = new Team();
        team.setId(id + " - team");
        team.setName("Team to support segment " + id);

        UserResolver userResolver = new UserResolver();
        userResolver.setActive(true);
        userResolver.setId("user1");
        userResolver.setName("John");
        userResolver.setLastName("Goodman");
        userResolver.setSource(1l);

        team.setUserResolver(userResolver);

        User user1 = new User();
        user1.setId("user 1");
        user1.setName("Bruce");
        user1.setLastName("Wayne");

        List<User> users = new ArrayList<>();
        users.add(user1);
        team.setUsers(users);

        return team;
    }

    private List<DetectionCriteria> getDetectionCriteriaBySegmentId(String id) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String initDateString = "31-12-2015 09:00:00";
        String endDateString = "31-12-2017 09:00:00";

        Date initDate = sdf.parse(initDateString);
        Date endDate = sdf.parse(endDateString);

        Filter filter = new Filter();
        filter.setId(id + "filter 1");
        MediaFilter mediaFilter = new MediaFilter();
        mediaFilter.setOptions(new Long[] { 1087L, 1086L });
        filter.setMediaFilter(mediaFilter);

        LanguageFilter languageFilter = new LanguageFilter();
        languageFilter.setOptions(new String[] { "es", "en", "pt" });
        filter.setLanguageFilter(languageFilter);

        List<DetectionCriteria> detectionCriteriasFromSegment = new ArrayList<>();
        DetectionCriteria criteria1 = new DetectionCriteria();
        criteria1.setSequenceOrder(1);
        criteria1.setId(id + "- criteria 1");
        criteria1.setFilter(filter);
        criteria1.setValidateFrom(initDate);
        criteria1.setValidateTo(endDate);
        criteria1.setAccionCriterias(getActionsCriteriaByDetectionCriteria(criteria1.getId()));

        DetectionCriteria criteria2 = new DetectionCriteria();
        criteria2.setSequenceOrder(2);
        criteria2.setId(id + "- criteria 2");
        criteria2.setFilter(filter);
        criteria2.setValidateFrom(initDate);
        criteria2.setValidateTo(endDate);
        criteria2.setAccionCriterias(getActionsCriteriaByDetectionCriteria(criteria2.getId()));

        DetectionCriteria criteria3 = new DetectionCriteria();
        criteria3.setSequenceOrder(3);
        criteria3.setId(id + "- criteria 3");
        criteria3.setFilter(filter);
        criteria3.setValidateFrom(initDate);
        criteria3.setValidateTo(endDate);
        criteria3.setAccionCriterias(getActionsCriteriaByDetectionCriteria(criteria3.getId()));

        detectionCriteriasFromSegment.add(criteria1);
        detectionCriteriasFromSegment.add(criteria2);
        detectionCriteriasFromSegment.add(criteria3);

        return detectionCriteriasFromSegment;
    }

    private List<AutomaticActionCriteria> getActionsCriteriaByDetectionCriteria(String id) {
        List<AutomaticActionCriteria> automaticActionsCriteria = new ArrayList<>();

        AutomaticActionCriteria actionCriteria = new AutomaticActionCriteria();
        actionCriteria.setActionType(ActionType.OPEN_CASE);
        actionCriteria.setId(id + "-actioncriteria 1");
        List<String> parameters1 = new ArrayList<>();
        parameters1.add("value 1");
        actionCriteria.setParameters(parameters1);

        AutomaticActionCriteria actionCriteria2 = new AutomaticActionCriteria();
        actionCriteria2.setActionType(ActionType.TAG_CASE);
        actionCriteria2.setId(id + "-actioncriteria 1");

        List<String> parameters2 = new ArrayList<>();
        parameters2.add("value 2");
        actionCriteria2.setParameters(parameters2);

        AutomaticActionCriteria actionCriteria3 = new AutomaticActionCriteria();
        actionCriteria3.setActionType(ActionType.ASSIGN);
        actionCriteria3.setId(id + "-actioncriteria 1");

        List<String> parameters3 = new ArrayList<>();
        parameters3.add("value 3");
        actionCriteria3.setParameters(parameters3);

        automaticActionsCriteria.add(actionCriteria);
        automaticActionsCriteria.add(actionCriteria2);
        automaticActionsCriteria.add(actionCriteria3);

        return automaticActionsCriteria;
    }

}
