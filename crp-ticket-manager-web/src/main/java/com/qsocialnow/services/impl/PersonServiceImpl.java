package com.qsocialnow.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.qsocialnow.common.model.cases.Person;
import com.qsocialnow.factories.RestTemplateFactory;
import com.qsocialnow.services.PersonService;
import com.qsocialnow.services.ServiceUrlResolver;

@Service("personService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PersonServiceImpl implements PersonService {

    private static final Logger log = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Value("${person.serviceurl}")
    private String personServiceUrl;

    @Autowired
    private ServiceUrlResolver serviceUrlResolver;

    @Override
    public Person findOne(String personId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                    serviceUrlResolver.resolveUrl(personServiceUrl)).path("/" + personId);

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            Person personSelected = restTemplate.getForObject(builder.toUriString(), Person.class);

            return personSelected;
        } catch (Exception e) {
            log.error("There was an error while trying to call Person service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Person update(Person currentPerson) {

        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                    serviceUrlResolver.resolveUrl(personServiceUrl)).path("/" + currentPerson.getId());

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Accept", "application/json");

            HttpEntity<Person> requestEntity = new HttpEntity<Person>(currentPerson, headers);
            ResponseEntity<Person> response = restTemplate.exchange(builder.toUriString(), HttpMethod.PUT,
                    requestEntity, Person.class);

            return response.getBody();
        } catch (Exception e) {
            log.error("There was an error while trying to request Person service", e);
            throw new RuntimeException(e);
        }
    }

}
