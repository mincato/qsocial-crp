package com.qsocialnow.elasticsearch.mappings.cases;

import org.json.simple.JSONObject;

import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.elasticsearch.mappings.DynamicMapping;
import com.qsocialnow.elasticsearch.mappings.Mapping;
import com.qsocialnow.elasticsearch.mappings.types.config.SubjectCategoryType;
import com.qsocialnow.elasticsearch.mappings.types.config.SubjectType;

public class SubjectMapping implements DynamicMapping,Mapping<SubjectType, Subject> {

    private static final String TYPE = "subject";
    
    private String indexName;

    private static SubjectMapping instance;

    private SubjectMapping() {
    }

    public static SubjectMapping getInstance() {
        if (instance == null)
            instance = new SubjectMapping();
        return instance;
    }
    
    @Override
	public void setIndex(String index) {
    	this.indexName = index;
	}
    
    @Override
    public String getIndex() {
        return indexName;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getMappingDefinition() {
        JSONObject mapping = new JSONObject();
        return mapping.toJSONString();
    }

    @Override
    public Class<?> getClassType() {
        return SubjectCategoryType.class;
    }

    @Override
    public SubjectType getDocumentType(Subject document) {
        SubjectType subjectType = new SubjectType();
        subjectType.setId(document.getId());
        subjectType.setAddress(document.getAddress());
        subjectType.setAge(document.getAge());
        subjectType.setContactInfo(document.getContactInfo());
        subjectType.setLastAccion(document.getLastAccion());
        
        subjectType.setLastName(document.getLastName());
        subjectType.setName(document.getName());
        subjectType.setSignedDate(document.getSignedDate());
        subjectType.setSourceId(document.getSourceId());
        subjectType.setSubjectCategory(document.getSubjectCategory());
        subjectType.setSubjectCategorySet(document.getSubjectCategorySet());
        return subjectType;
    }

    @Override
    public Subject getDocument(SubjectType documentType) {
        Subject subject = new Subject();
        subject.setId(documentType.getId());
        subject.setAddress(documentType.getAddress());
        
        subject.setAge(documentType.getAge());
        subject.setContactInfo(documentType.getContactInfo());
        subject.setLastAccion(documentType.getLastAccion());
        
        subject.setLastName(documentType.getLastName());
        subject.setName(documentType.getName());
        subject.setSignedDate(documentType.getSignedDate());
        subject.setSourceId(documentType.getSourceId());
        subject.setSubjectCategory(documentType.getSubjectCategory());
        subject.setSubjectCategorySet(documentType.getSubjectCategorySet());
        return subject;
    }
}
