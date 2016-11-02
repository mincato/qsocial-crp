package com.qsocialnow.retroactiveprocess.config;

public class PagedEventsServiceConfig {

    private static final Integer DEFAULT_MAX_RESULTS = 100;

    private Integer scrollExpirationDuration;

    private Integer maxResults = DEFAULT_MAX_RESULTS;

    public Integer getScrollExpirationDuration() {
        return scrollExpirationDuration;
    }

    public void setScrollExpirationDuration(Integer scrollExpirationDuration) {
        this.scrollExpirationDuration = scrollExpirationDuration;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

}
