package com.qsocialnow.services.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.qsocialnow.common.model.config.UserListView;
import com.qsocialnow.config.Organization;
import com.qsocialnow.factories.RestTemplateFactory;
import com.qsocialnow.services.ServiceUrlResolver;
import com.qsocialnow.services.UserService;

@Service("userService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserServiceImpl implements UserService {

    private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${user.serviceurl}")
    private String userServiceUrl;

    @Autowired
    private ServiceUrlResolver serviceUrlResolver;

    @Override
    public List<UserListView> findAll(Map<String, String> filters) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serviceUrlResolver.resolveUrl(
                    Organization.ODATECH, userServiceUrl));

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            ResponseEntity<List<UserListView>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
                    null, new ParameterizedTypeReference<List<UserListView>>() {
                    });

            List<UserListView> users = response.getBody();
            return users;
        } catch (Exception e) {
            log.error("There was an error while trying to call user service", e);
            throw new RuntimeException(e);
        }
    }
}
