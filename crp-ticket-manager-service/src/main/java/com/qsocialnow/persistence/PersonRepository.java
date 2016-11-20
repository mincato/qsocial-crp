package com.qsocialnow.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.cases.Person;
import com.qsocialnow.elasticsearch.services.cases.PersonService;

@Service
public class PersonRepository {

    @Autowired
    private PersonService personElasticService;

    public Person findOne(String personId) {
        return personElasticService.findPersonById(personId);
    }

    public boolean update(Person personObject) {
        String id = personElasticService.update(personObject);
        return id != null;
    }
}
