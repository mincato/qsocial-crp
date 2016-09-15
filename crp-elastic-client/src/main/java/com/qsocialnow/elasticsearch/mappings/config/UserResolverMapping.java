package com.qsocialnow.elasticsearch.mappings.config;

import org.json.simple.JSONObject;

import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.elasticsearch.mappings.Mapping;
import com.qsocialnow.elasticsearch.mappings.types.config.TriggerType;
import com.qsocialnow.elasticsearch.mappings.types.config.UserResolverType;

public class UserResolverMapping implements Mapping<UserResolverType, UserResolver> {

    private static final String INDEX_NAME = "configuration";

    private static final String TYPE = "userResolver";

    private static UserResolverMapping instance;

    private UserResolverMapping() {
    }

    public static UserResolverMapping getInstance() {
        if (instance == null)
            instance = new UserResolverMapping();
        return instance;
    }

    @Override
    public String getIndex() {
        return INDEX_NAME;
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
        return TriggerType.class;
    }

    @Override
    public UserResolverType getDocumentType(UserResolver document) {
        UserResolverType userResolverType = new UserResolverType();
        userResolverType.setActive(document.getActive());
        userResolverType.setCredentials(document.getCredentials());
        userResolverType.setIdentifier(document.getIdentifier());
        userResolverType.setSource(document.getSource());
        return userResolverType;
    }

    @Override
    public UserResolver getDocument(UserResolverType documentType) {
        UserResolver userResolver = new UserResolver();
        userResolver.setId(documentType.getId());
        userResolver.setActive(documentType.getActive());
        userResolver.setCredentials(documentType.getCredentials());
        userResolver.setIdentifier(documentType.getIdentifier());
        userResolver.setSource(documentType.getSource());
        return userResolver;
    }

}
