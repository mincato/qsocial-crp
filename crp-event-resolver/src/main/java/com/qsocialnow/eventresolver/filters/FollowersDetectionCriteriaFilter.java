package com.qsocialnow.eventresolver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

@Component("followersDetectionCriteriaFilter")
public class FollowersDetectionCriteriaFilter implements DetectionCriteriaFilter {

    private static final Logger log = LoggerFactory.getLogger(FollowersDetectionCriteriaFilter.class);

    @Override
    public boolean match(NormalizedInputBeanDocument message, Filter filter) {
        log.info("Executing followers message filter");
        boolean match = false;
        if (message.getFollowersCount() != null) {
            Long minFollowers = filter.getFollowersFilter().getMinFollowers();
            Long maxFollowers = filter.getFollowersFilter().getMaxFollowers();
            Long messageFollowers = message.getFollowersCount();
            if (minFollowers != null && maxFollowers != null) {
                match = minFollowers < messageFollowers && messageFollowers < maxFollowers;
            } else if (minFollowers != null) {
                match = minFollowers < messageFollowers;
            } else if (maxFollowers != null) {
                match = messageFollowers < maxFollowers;
            }
        }
        return match;
    }

    @Override
    public boolean apply(Filter filter) {
        return filter.getFollowersFilter() != null
                && (filter.getFollowersFilter().getMaxFollowers() != null || filter.getFollowersFilter()
                        .getMinFollowers() != null);
    }

}
