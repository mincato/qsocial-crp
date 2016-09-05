package com.qsocialnow.common.model.config;

public class FollowersFilter {

    private Long minFollowers;

    private Long maxFollowers;

    public Long getMaxFollowers() {
        return maxFollowers;
    }

    public void setMaxFollowers(Long maxFollowers) {
        this.maxFollowers = maxFollowers;
    }

    public Long getMinFollowers() {
        return minFollowers;
    }

    public void setMinFollowers(Long minFollowers) {
        this.minFollowers = minFollowers;
    }
}
