package com.qsocialnow.autoscaling.config;

public interface RefreshableConfig<T> {

    public void refresh(T newConfig);

}
