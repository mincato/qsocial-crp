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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.qsocialnow.common.model.config.BaseUserResolver;
import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.common.model.config.TeamListView;
import com.qsocialnow.common.model.config.User;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.factories.RestTemplateFactory;
import com.qsocialnow.services.ServiceUrlResolver;
import com.qsocialnow.services.TeamService;

@Service("teamService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TeamServiceImpl implements TeamService {

    private static final Logger log = LoggerFactory.getLogger(TeamServiceImpl.class);

    @Value("${team.serviceurl}")
    private String teamServiceUrl;

    @Value("${teamActive.serviceurl}")
    private String teamActiveServiceUrl;

    @Autowired
    private ServiceUrlResolver serviceUrlResolver;

    public Team create(Team team) {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            Team createdTeam = restTemplate.postForObject(serviceUrlResolver.resolveUrl(teamServiceUrl), team,
                    Team.class);
            return createdTeam;
        } catch (Exception e) {
            log.error("There was an error while trying to call team service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Team findOne(String teamId) {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                    serviceUrlResolver.resolveUrl(teamServiceUrl)).path("/" + teamId);
            Team team = restTemplate.getForObject(builder.toUriString(), Team.class);
            return team;
        } catch (Exception e) {
            log.error("There was an error while trying to call team service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Team update(Team currentTeam) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                    serviceUrlResolver.resolveUrl(teamServiceUrl)).path("/" + currentTeam.getId());

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Accept", "application/json");

            HttpEntity<Team> requestEntity = new HttpEntity<Team>(currentTeam, headers);

            ResponseEntity<Team> response = restTemplate.exchange(builder.toUriString(), HttpMethod.PUT, requestEntity,
                    Team.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("There was an error while trying to call team service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResponse<TeamListView> findAll(int pageNumber, int pageSize, Map<String, String> filters) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(teamServiceUrl)).path("/list")
                    .queryParam("pageNumber", pageNumber).queryParam("pageSize", pageSize);

            if (filters != null) {
                for (Map.Entry<String, String> filter : filters.entrySet()) {
                    builder.queryParam(filter.getKey(), filter.getValue());
                }
            }

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            ResponseEntity<PageResponse<TeamListView>> response = restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET, null, new ParameterizedTypeReference<PageResponse<TeamListView>>() {
                    });

            PageResponse<TeamListView> teams = response.getBody();
            return teams;
        } catch (Exception e) {
            log.error("There was an error while trying to call team service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TeamListView> findAll() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serviceUrlResolver
                    .resolveUrl(teamServiceUrl));

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            ResponseEntity<List<TeamListView>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
                    null, new ParameterizedTypeReference<List<TeamListView>>() {
                    });

            List<TeamListView> teams = response.getBody();
            return teams;
        } catch (Exception e) {
            log.error("There was an error while trying to call team service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TeamListView> findAllActive() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serviceUrlResolver
                    .resolveUrl(teamActiveServiceUrl));

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            ResponseEntity<List<TeamListView>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
                    null, new ParameterizedTypeReference<List<TeamListView>>() {
                    });

            List<TeamListView> teams = response.getBody();
            return teams;
        } catch (Exception e) {
            log.error("There was an error while trying to call team service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<BaseUserResolver> findUserResolvers(String teamId, Map<String, Object> filters) {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(teamServiceUrl)).path("/" + teamId)
                    .path("/userResolvers");

            if (filters != null) {
                for (Map.Entry<String, Object> filter : filters.entrySet()) {
                    builder.queryParam(filter.getKey(), filter.getValue());
                }
            }

            ResponseEntity<List<BaseUserResolver>> response = restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<BaseUserResolver>>() {
                    });
            return response.getBody();
        } catch (Exception e) {
            log.error("There was an error while trying to call team service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> findUsers(String teamId) {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(teamServiceUrl)).path("/" + teamId).path("/users");

            ResponseEntity<List<User>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<User>>() {
                    });
            return response.getBody();
        } catch (Exception e) {
            log.error("There was an error while trying to call team service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> findAllActiveIdsByTeam(String teamId) {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(teamServiceUrl)).path("/" + teamId)
                    .path("/segmentsActive");

            ResponseEntity<List<String>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<String>>() {
                    });
            return response.getBody();
        } catch (Exception e) {
            log.error("There was an error while trying to call team service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reassign(String oldTeamId, String newTeamId) {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(teamServiceUrl)).path("/" + oldTeamId)
                    .path("/reassignSegment/" + newTeamId);

            restTemplate.exchange(builder.toUriString(), HttpMethod.POST, null,
                    new ParameterizedTypeReference<String>() {
                    });

        } catch (Exception e) {
            log.error("There was an error while trying to call team service", e);
            throw new RuntimeException(e);
        }
    }
}
