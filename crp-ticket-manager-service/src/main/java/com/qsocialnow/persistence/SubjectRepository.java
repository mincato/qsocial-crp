package com.qsocialnow.persistence;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.common.model.cases.SubjectListView;
import com.qsocialnow.common.model.request.SubjectListRequest;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.elasticsearch.services.cases.SubjectService;

@Service
public class SubjectRepository {

    private Logger log = LoggerFactory.getLogger(SubjectRepository.class);

    @Autowired
    private SubjectService subjectElasticService;

    public List<SubjectListView> findAll(PageRequest pageRequest, SubjectListRequest subjectListRequest) {
        List<SubjectListView> subjects = new ArrayList<>();

        try {
            List<Subject> subjectsRepo = subjectElasticService.findSubjects(pageRequest.getOffset(),
                    pageRequest.getLimit(), subjectListRequest);
            if (subjectsRepo != null) {
                for (Subject subjectRepo : subjectsRepo) {
                    SubjectListView subjectListView = new SubjectListView();
                    subjectListView.setId(subjectRepo.getId());
                    subjectListView.setName(subjectRepo.getName());
                    subjectListView.setLastName(subjectRepo.getLastName());
                    subjectListView.setIdentifier(subjectRepo.getIdentifier());
                    subjectListView.setSource(subjectRepo.getSource());
                    subjects.add(subjectListView);
                }
            }
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return subjects;
    }

    public Long count() {
        return 50L;
    }

    public Subject findOne(String subjectId) {
        return subjectElasticService.findSubjectById(subjectId);
    }

    public Subject save(Subject subjectObject) {
        try {
            String id = subjectElasticService.indexSubject(subjectObject);
            subjectObject.setId(id);
            log.info("Subject:", subjectObject.getName());
            return subjectObject;
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

    public boolean update(Subject subjectObject) {
        String id = subjectElasticService.update(subjectObject);
        return id != null;
    }

}
