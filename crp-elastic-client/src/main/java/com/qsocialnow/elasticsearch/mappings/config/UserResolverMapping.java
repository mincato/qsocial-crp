package com.qsocialnow.elasticsearch.mappings.config;

import org.json.simple.JSONObject;

import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.elasticsearch.mappings.Mapping;
import com.qsocialnow.elasticsearch.mappings.types.config.UserResolverType;

public class UserResolverMapping implements Mapping<UserResolverType, UserResolver> {

    private static final String TYPE = "userResolver";

    private static UserResolverMapping instance;

    private final String index;

    private UserResolverMapping(String index) {
        this.index = index;
    }

    public static UserResolverMapping getInstance(String index) {
        if (instance == null)
            instance = new UserResolverMapping(index);
        return instance;
    }

    @Override
    public String getIndex() {
        return index;
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
        return UserResolverType.class;
    }

    @Override
    public UserResolverType getDocumentType(UserResolver document) {
        UserResolverType userResolverType = new UserResolverType();
        userResolverType.setActive(document.isActive());
        userResolverType.setCredentials(document.getCredentials());
        userResolverType.setIdentifier(document.getIdentifier());
        userResolverType.setSource(document.getSource());
        userResolverType.setSourceId(document.getSourceId());
        return userResolverType;
    }

    @Override
    public UserResolver getDocument(UserResolverType documentType) {
        UserResolver userResolver = new UserResolver();
        userResolver.setId(documentType.getId());
        userResolver.setActive(documentType.isActive());
        userResolver.setCredentials(documentType.getCredentials());
        userResolver.setIdentifier(documentType.getIdentifier());
        userResolver.setSource(documentType.getSource());
        userResolver.setSourceId(documentType.getSourceId());
        return userResolver;
    }

}
