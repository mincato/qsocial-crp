package com.qsocialnow.eventresolver.filters.matchers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.qsocialnow.common.model.event.Event;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

public class AuthorWordFilterMatcherTest {

    private AuthorWordFilterMatcher matcher;
    private Set<String> filterTexts;

    public AuthorWordFilterMatcherTest() {
        matcher = new AuthorWordFilterMatcher();
        filterTexts = new HashSet<>();
    }

    @Test
    public void testMatchInputNull() {
        Event input = new Event();

        filterTexts.addAll(Arrays.asList("messi", "ronaldo"));

        Assert.assertFalse(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchTrueUsuarioCreacion() {
        Event input = new Event();
        input.setUsuarioCreacion("messi");

        filterTexts.addAll(Arrays.asList("messi", "ronaldo"));

        Assert.assertTrue(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchTrueUsuarioReproduccion() {
        Event input = new Event();
        input.setUsuarioReproduccion("ronaldo");

        filterTexts.addAll(Arrays.asList("messi", "ronaldo"));

        Assert.assertTrue(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchTrueUsuarioCreacionWithUrl() {
        Event input = new Event();
        input.setUsuarioCreacion("messi http://twitter.com/messi");

        filterTexts.addAll(Arrays.asList("messi", "ronaldo"));

        Assert.assertTrue(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchTrueUsuarioReproduccionWithUrl() {
        Event input = new Event();
        input.setUsuarioReproduccion("ronaldo https://twitter.com/ronaldo");

        filterTexts.addAll(Arrays.asList("messi", "ronaldo"));

        Assert.assertTrue(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchTrueUsuarioCreacionFalseUsuarioReproduccionTrue() {
        Event input = new Event();
        input.setUsuarioCreacion("tevez");
        input.setUsuarioReproduccion("ronaldo");

        filterTexts.addAll(Arrays.asList("messi", "ronaldo"));

        Assert.assertTrue(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchFalseUsuarioCreacion() {
        Event input = new Event();
        input.setUsuarioCreacion("tevez");

        filterTexts.addAll(Arrays.asList("messi", "ronaldo"));

        Assert.assertFalse(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchFalseUsuarioCreacionWithUrl() {
        Event input = new Event();
        input.setUsuarioCreacion("tevez http://twitter.com/messi");

        filterTexts.addAll(Arrays.asList("messi", "ronaldo"));

        Assert.assertFalse(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchFalseUsuarioReproduccion() {
        Event input = new Event();
        input.setUsuarioReproduccion("roman");

        filterTexts.addAll(Arrays.asList("messi", "ronaldo"));

        Assert.assertFalse(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchFalseUsuarioReproduccionWithUrl() {
        Event input = new Event();
        input.setUsuarioReproduccion("roman http://twitter.com/ronaldo");

        filterTexts.addAll(Arrays.asList("messi", "ronaldo"));

        Assert.assertFalse(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchFalseUsuarioCreacionUsuarioReproduccion() {
        Event input = new Event();
        input.setUsuarioCreacion("tevez");
        input.setUsuarioReproduccion("roman");

        filterTexts.addAll(Arrays.asList("messi", "ronaldo"));

        Assert.assertFalse(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchFalseUsuarioCreacionUsuarioReproduccionWithUrl() {
        Event input = new Event();
        input.setUsuarioCreacion("tevez http://twitter.com/messi");
        input.setUsuarioReproduccion("roman http://twitter.com/ronaldo");

        filterTexts.addAll(Arrays.asList("messi", "ronaldo"));

        Assert.assertFalse(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

}
