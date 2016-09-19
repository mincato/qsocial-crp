package com.qsocialnow.elasticsearch.mappings.types.config;

import com.qsocialnow.common.model.config.UserResolver;

import io.searchbox.annotations.JestId;

public class UserResolverType extends UserResolver {

    @JestId
    private String idUserResolver;

    @Override
    public String getId() {
        return this.idUserResolver;
    }

}
