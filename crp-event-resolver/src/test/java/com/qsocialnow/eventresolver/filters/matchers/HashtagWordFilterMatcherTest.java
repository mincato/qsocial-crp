package com.qsocialnow.eventresolver.filters.matchers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.qsocialnow.common.model.event.InPutBeanDocument;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

public class HashtagWordFilterMatcherTest {

    private HashtagWordFilterMatcher matcher;
    private Set<String> filterTexts;

    public HashtagWordFilterMatcherTest() {
        matcher = new HashtagWordFilterMatcher();
        filterTexts = new HashSet<>();
    }

    @Test
    public void testMatchInputNull() {
        InPutBeanDocument input = new InPutBeanDocument();

        filterTexts.addAll(Arrays.asList("#messi", "#ronaldo"));

        Assert.assertFalse(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchTrue() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setHashTags(new String[] { "#messi", "#ronaldo", "#tevez" });

        filterTexts.addAll(Arrays.asList("#messi"));

        Assert.assertTrue(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchTrueMultipleValues() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setHashTags(new String[] { "#messi", "#ronaldo", "#tevez" });

        filterTexts.addAll(Arrays.asList("#messi", "#ronaldo"));

        Assert.assertTrue(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchTrueUpperCase() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setHashTags(new String[] { "#MesSi", "#Ronaldo", "#Tevez" });

        filterTexts.addAll(Arrays.asList("#messi"));

        Assert.assertTrue(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchTrueMultipleValuesUpperCase() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setHashTags(new String[] { "#MesSi", "#RONALDO", "#Tevez" });

        filterTexts.addAll(Arrays.asList("#messi", "#ronaldo"));

        Assert.assertTrue(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchFalse() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setHashTags(new String[] { "#messi", "#ronaldo", "#tevez" });

        filterTexts.addAll(Arrays.asList("#roman"));

        Assert.assertFalse(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchFalseMultipleValues() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setHashTags(new String[] { "#messi", "#ronaldo", "#tevez" });

        filterTexts.addAll(Arrays.asList("#messi", "#roman"));

        Assert.assertFalse(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

}
