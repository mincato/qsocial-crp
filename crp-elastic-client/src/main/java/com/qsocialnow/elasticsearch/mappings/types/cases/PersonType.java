package com.qsocialnow.elasticsearch.mappings.types.cases;

import com.qsocialnow.common.model.cases.Person;

import io.searchbox.annotations.JestId;

public class PersonType extends Person implements IdentityType {

    private static final long serialVersionUID = 133524547152523564L;

    @JestId
    private String idPerson;

    @Override
    public String getId() {
        return this.idPerson;
    }

}
