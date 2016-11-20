package com.qsocialnow.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.cases.Person;
import com.qsocialnow.persistence.PersonRepository;

@Service
public class PersonService {

    private static final Logger log = LoggerFactory.getLogger(PersonService.class);

    @Autowired
    private PersonRepository personRepository;

    public Person findOne(String personId) {
        Person person = personRepository.findOne(personId);
        return person;
    }

    public Person update(String personId, Person person) {
        try {
            log.info("Updating perdon : " + personId);
            if (personRepository.update(person))
                return person;

        } catch (Exception e) {
            log.error("There was an error updating Person: " + person.getName(), e);
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

}
