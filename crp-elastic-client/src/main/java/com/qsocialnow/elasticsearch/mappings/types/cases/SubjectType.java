package com.qsocialnow.elasticsearch.mappings.types.cases;

import com.qsocialnow.common.model.cases.Subject;

import io.searchbox.annotations.JestId;

public class SubjectType extends Subject implements IdentityType {

	private static final long serialVersionUID = 6792148307388838666L;
	
	@JestId
    private String idSubject;

    @Override
    public String getId() {
        return this.idSubject;
    }
}
