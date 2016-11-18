package com.qsocialnow.elasticsearch.mappings.cases;

import org.json.simple.JSONObject;

import com.qsocialnow.common.model.cases.Person;
import com.qsocialnow.elasticsearch.mappings.DynamicMapping;
import com.qsocialnow.elasticsearch.mappings.Mapping;
import com.qsocialnow.elasticsearch.mappings.types.cases.PersonType;

public class PersonMapping implements DynamicMapping, Mapping<PersonType, Person> {

    private static final String TYPE = "person";

    private String indexName;

    private static PersonMapping instance;

    private PersonMapping() {
    }

    public static PersonMapping getInstance() {
        if (instance == null) {
            instance = new PersonMapping();
        }
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
        return PersonType.class;
    }

    @Override
    public PersonType getDocumentType(Person document) {
        PersonType personType = new PersonType();
        personType.setId(document.getId());
        personType.setAddress(document.getAddress());
        personType.setAge(document.getAge());
        personType.setName(document.getName());
        personType.setLastName(document.getLastName());
        personType.setContactInfo(document.getContactInfo());
        personType.setSignedDate(document.getSignedDate());
        return personType;
    }

    @Override
    public Person getDocument(PersonType personType) {
        Person person = new Person();
        person.setId(personType.getId());
        person.setAddress(personType.getAddress());
        person.setContactInfo(personType.getContactInfo());
        person.setAge(personType.getAge());
        person.setName(personType.getName());
        person.setLastName(person.getLastName());
        person.setSignedDate(personType.getSignedDate());
        return person;
    }
}
