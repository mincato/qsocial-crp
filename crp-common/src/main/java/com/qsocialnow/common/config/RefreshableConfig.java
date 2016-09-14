package com.qsocialnow.common.config;

public interface RefreshableConfig<T> {

    public void refresh(T newConfig);

}
