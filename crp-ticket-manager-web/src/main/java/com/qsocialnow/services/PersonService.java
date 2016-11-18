package com.qsocialnow.services;

import com.qsocialnow.common.model.cases.Person;

public interface PersonService {

    Person findOne(String personId);

    Person update(Person currentPerson);

}
