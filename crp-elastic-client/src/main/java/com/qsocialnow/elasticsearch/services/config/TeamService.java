package com.qsocialnow.elasticsearch.services.config;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.elasticsearch.configuration.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.elasticsearch.mappings.config.TeamMapping;
import com.qsocialnow.elasticsearch.mappings.types.config.TeamType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

public class TeamService {

    private AWSElasticsearchConfigurationProvider configurator;

    private ConfigurationIndexService indexConfiguration;

    private Logger log = LoggerFactory.getLogger(TeamService.class);

    public String indexTeam(Team team) {
        RepositoryFactory<TeamType> esfactory = new RepositoryFactory<TeamType>(configurator);

        Repository<TeamType> repository = esfactory.initManager();
        repository.initClient();

        TeamMapping mapping = TeamMapping.getInstance(indexConfiguration.getIndexName());

        // index document
        TeamType documentIndexed = mapping.getDocumentType(team);
        String response = repository.indexMapping(mapping, documentIndexed);
        repository.closeClient();
        return response;
    }

    public List<Team> getTeams(Integer offset, Integer limit, String name) {
        RepositoryFactory<TeamType> esfactory = new RepositoryFactory<TeamType>(configurator);
        Repository<TeamType> repository = esfactory.initManager();
        repository.initClient();

        TeamMapping mapping = TeamMapping.getInstance(indexConfiguration.getIndexName());

        BoolQueryBuilder filters = null;
        if (name != null) {
            filters = QueryBuilders.boolQuery();
            filters = filters.must(QueryBuilders.matchQuery("name", name));
        }

        SearchResponse<Team> response = repository.searchWithFilters(offset, limit, "name", filters, mapping);
        List<Team> teams = response.getSources();

        repository.closeClient();
        return teams;
    }

    public Team findOne(String teamId) {
        RepositoryFactory<TeamType> esfactory = new RepositoryFactory<TeamType>(configurator);
        Repository<TeamType> repository = esfactory.initManager();
        repository.initClient();

        TeamMapping mapping = TeamMapping.getInstance(indexConfiguration.getIndexName());

        SearchResponse<Team> response = repository.find(teamId, mapping);

        Team team = response.getSource();

        repository.closeClient();
        return team;
    }

    public void setConfigurator(AWSElasticsearchConfigurationProvider configurator) {
        this.configurator = configurator;
    }

    public String updateTeam(Team team) {
        RepositoryFactory<TeamType> esfactory = new RepositoryFactory<TeamType>(configurator);

        Repository<TeamType> repository = esfactory.initManager();
        repository.initClient();

        TeamMapping mapping = TeamMapping.getInstance(indexConfiguration.getIndexName());
        TeamType documentIndexed = mapping.getDocumentType(team);
        String response = repository.updateMapping(team.getId(), mapping, documentIndexed);
        repository.closeClient();
        return response;
    }

    public void setIndexConfiguration(ConfigurationIndexService indexConfiguration) {
        this.indexConfiguration = indexConfiguration;
    }

    public List<Team> findTeamsByUser(String userName) {
        RepositoryFactory<TeamType> esfactory = new RepositoryFactory<TeamType>(configurator);
        Repository<TeamType> repository = esfactory.initManager();
        repository.initClient();
        log.info("Repository from teams -from index: " + indexConfiguration.getIndexName() + " retrieving from user :"
                + userName);

        TeamMapping mapping = TeamMapping.getInstance(indexConfiguration.getIndexName());
        BoolQueryBuilder filters = null;

        if (userName != null) {
            filters = QueryBuilders.boolQuery();
            filters = filters.must(QueryBuilders.matchQuery("users.username", "jperez"));
        }
        SearchResponse<Team> response = repository.searchWithFilters(null, null, null, null, mapping);
        List<Team> teams = response.getSources();

        repository.closeClient();
        return teams;
    }

}
