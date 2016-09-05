package com.qsocialnow.eventresolver.filters;

import org.junit.Assert;
import org.junit.Test;

import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.common.model.config.FollowersFilter;
import com.qsocialnow.common.model.event.InPutBeanDocument;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

public class FollowersDetectionCriteriaFilterTest {

    private FollowersDetectionCriteriaFilter filter;

    private Filter filterConfig;

    public FollowersDetectionCriteriaFilterTest() {
        filter = new FollowersDetectionCriteriaFilter();
        filterConfig = new Filter();
    }

    @Test
    public void testApplyTrueBoth() {
        FollowersFilter followersFilter = new FollowersFilter();
        followersFilter.setMinFollowers(1l);
        followersFilter.setMaxFollowers(5l);
        filterConfig.setFollowersFilter(followersFilter);

        Assert.assertTrue(filter.apply(filterConfig));
    }

    @Test
    public void testApplyTrueMin() {
        FollowersFilter followersFilter = new FollowersFilter();
        followersFilter.setMinFollowers(1l);
        filterConfig.setFollowersFilter(followersFilter);

        Assert.assertTrue(filter.apply(filterConfig));
    }

    @Test
    public void testApplyTrueMax() {
        FollowersFilter followersFilter = new FollowersFilter();
        followersFilter.setMaxFollowers(5l);
        filterConfig.setFollowersFilter(followersFilter);

        Assert.assertTrue(filter.apply(filterConfig));
    }

    @Test
    public void testApplyFalseNullFollowersFilter() {
        Assert.assertFalse(filter.apply(filterConfig));
    }

    @Test
    public void testApplyFalseNullMaxAndMin() {
        FollowersFilter followersFilter = new FollowersFilter();
        filterConfig.setFollowersFilter(followersFilter);

        Assert.assertFalse(filter.apply(filterConfig));
    }

    @Test
    public void testMatchInputNull() {
        InPutBeanDocument input = new InPutBeanDocument();

        FollowersFilter followersFilter = new FollowersFilter();
        followersFilter.setMinFollowers(2l);
        followersFilter.setMaxFollowers(7l);
        filterConfig.setFollowersFilter(followersFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchTrueBetweenRange() throws Exception {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setFollowersCount(5l);

        FollowersFilter followersFilter = new FollowersFilter();
        followersFilter.setMinFollowers(2l);
        followersFilter.setMaxFollowers(7l);
        filterConfig.setFollowersFilter(followersFilter);

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseBetweenRangeMaxEquals() throws Exception {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setFollowersCount(5l);

        FollowersFilter followersFilter = new FollowersFilter();
        followersFilter.setMinFollowers(2l);
        followersFilter.setMaxFollowers(5l);
        filterConfig.setFollowersFilter(followersFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseBetweenRangeMinEquals() throws Exception {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setFollowersCount(2l);

        FollowersFilter followersFilter = new FollowersFilter();
        followersFilter.setMinFollowers(2l);
        followersFilter.setMaxFollowers(5l);
        filterConfig.setFollowersFilter(followersFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchTrueGreaterThan() throws Exception {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setFollowersCount(5l);

        FollowersFilter followersFilter = new FollowersFilter();
        followersFilter.setMinFollowers(2l);
        filterConfig.setFollowersFilter(followersFilter);

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseGreaterThanEquals() throws Exception {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setFollowersCount(5l);

        FollowersFilter followersFilter = new FollowersFilter();
        followersFilter.setMinFollowers(5l);
        filterConfig.setFollowersFilter(followersFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchTrueLessThan() throws Exception {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setFollowersCount(5l);

        FollowersFilter followersFilter = new FollowersFilter();
        followersFilter.setMaxFollowers(7l);
        filterConfig.setFollowersFilter(followersFilter);

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseLessThanEquals() throws Exception {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setFollowersCount(5l);

        FollowersFilter followersFilter = new FollowersFilter();
        followersFilter.setMaxFollowers(5l);
        filterConfig.setFollowersFilter(followersFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseBetweenRangeLower() throws Exception {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setFollowersCount(5l);

        FollowersFilter followersFilter = new FollowersFilter();
        followersFilter.setMinFollowers(1l);
        followersFilter.setMaxFollowers(4l);
        filterConfig.setFollowersFilter(followersFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseBetweenRangeHigher() throws Exception {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setFollowersCount(5l);

        FollowersFilter followersFilter = new FollowersFilter();
        followersFilter.setMinFollowers(6l);
        followersFilter.setMaxFollowers(8l);
        filterConfig.setFollowersFilter(followersFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseGreaterThan() throws Exception {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setFollowersCount(5l);

        FollowersFilter followersFilter = new FollowersFilter();
        followersFilter.setMinFollowers(7l);
        filterConfig.setFollowersFilter(followersFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseLessThan() throws Exception {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setFollowersCount(5l);

        FollowersFilter followersFilter = new FollowersFilter();
        followersFilter.setMaxFollowers(3l);
        filterConfig.setFollowersFilter(followersFilter);

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

}
