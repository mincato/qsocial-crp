package com.qsocialnow.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.common.model.cases.SubjectFilterRequest;
import com.qsocialnow.common.model.cases.SubjectListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.common.model.request.SubjectListRequest;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.persistence.SubjectRepository;

@Service
public class SubjectService {

    private static final Logger log = LoggerFactory.getLogger(SubjectService.class);

    @Autowired
    private SubjectRepository subjectRepository;

    public PageResponse<SubjectListView> findAll(SubjectFilterRequest filterRequest) {

        List<SubjectListView> subjects = subjectRepository.findAll(
                filterRequest.getPageRequest(),
                new SubjectListRequest(filterRequest.getKeyword(), filterRequest.getIdentifier(), filterRequest
                        .getSourceId(), filterRequest.getSourceName()));

        PageResponse<SubjectListView> page = new PageResponse<SubjectListView>(subjects, filterRequest.getPageRequest()
                .getPageNumber(), filterRequest.getPageRequest().getPageSize());
        return page;
    }

    public PageResponse<SubjectListView> verify(SubjectFilterRequest filterRequest) {
        PageRequest pageRequest = filterRequest.getPageRequest();

        List<SubjectListView> subjects = subjectRepository.verify(new PageRequest(pageRequest.getPageNumber(),
                pageRequest.getPageSize(), null),
                new SubjectListRequest(null, filterRequest.getIdentifier(), String.valueOf(filterRequest.getSource()),
                        filterRequest.getSourceName()));

        PageResponse<SubjectListView> page = new PageResponse<SubjectListView>(subjects, pageRequest.getPageNumber(),
                pageRequest.getPageSize());
        return page;
    }

    public Subject findOne(String subjectId) {
        Subject subject = subjectRepository.findOne(subjectId);
        return subject;
    }

    public Subject createSubject(Subject subject) {
        Subject subjectSaved = null;
        try {
            subjectSaved = subjectRepository.save(subject);
            if (subjectSaved.getId() == null) {
                throw new Exception("There was an error creating Subject: " + subject.getSourceName());
            }
        } catch (Exception e) {
            log.error("There was an error creating Subject: " + subject.getSourceName(), e);
            throw new RuntimeException(e.getMessage());
        }
        return subjectSaved;
    }

    public Subject update(String subjectId, Subject subject) {
        try {
            log.info("Updating subject : " + subjectId);
            if (subjectRepository.update(subject))
                return subject;

        } catch (Exception e) {
            log.error("There was an error updating Subject: " + subject.getSourceName(), e);
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

}
