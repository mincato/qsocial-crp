package com.qsocialnow.viewmodel.subject;

import javax.validation.Valid;

import com.qsocialnow.common.model.cases.Subject;

public class SubjectView {

    @Valid
    private Subject subject;

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}
    
    

}
