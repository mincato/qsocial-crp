package com.qsocialnow.eventresolver.filters.matchers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.qsocialnow.common.model.event.InPutBeanDocument;
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
        InPutBeanDocument input = new InPutBeanDocument();

        filterTexts.addAll(Arrays.asList("messi", "ronaldo"));

        Assert.assertFalse(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchTrueUsuarioCreacion() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setUsuarioCreacion("messi");

        filterTexts.addAll(Arrays.asList("messi", "ronaldo"));

        Assert.assertTrue(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchTrueUsuarioReproduccion() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setUsuarioReproduccion("ronaldo");

        filterTexts.addAll(Arrays.asList("messi", "ronaldo"));

        Assert.assertTrue(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchTrueUsuarioCreacionWithUrl() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setUsuarioCreacion("messi http://twitter.com/messi");

        filterTexts.addAll(Arrays.asList("messi", "ronaldo"));

        Assert.assertTrue(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchTrueUsuarioReproduccionWithUrl() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setUsuarioReproduccion("ronaldo https://twitter.com/ronaldo");

        filterTexts.addAll(Arrays.asList("messi", "ronaldo"));

        Assert.assertTrue(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchTrueUsuarioCreacionFalseUsuarioReproduccionTrue() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setUsuarioCreacion("tevez");
        input.setUsuarioReproduccion("ronaldo");

        filterTexts.addAll(Arrays.asList("messi", "ronaldo"));

        Assert.assertTrue(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchFalseUsuarioCreacion() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setUsuarioCreacion("tevez");

        filterTexts.addAll(Arrays.asList("messi", "ronaldo"));

        Assert.assertFalse(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchFalseUsuarioCreacionWithUrl() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setUsuarioCreacion("tevez http://twitter.com/messi");

        filterTexts.addAll(Arrays.asList("messi", "ronaldo"));

        Assert.assertFalse(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchFalseUsuarioReproduccion() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setUsuarioReproduccion("roman");

        filterTexts.addAll(Arrays.asList("messi", "ronaldo"));

        Assert.assertFalse(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchFalseUsuarioReproduccionWithUrl() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setUsuarioReproduccion("roman http://twitter.com/ronaldo");

        filterTexts.addAll(Arrays.asList("messi", "ronaldo"));

        Assert.assertFalse(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchFalseUsuarioCreacionUsuarioReproduccion() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setUsuarioCreacion("tevez");
        input.setUsuarioReproduccion("roman");

        filterTexts.addAll(Arrays.asList("messi", "ronaldo"));

        Assert.assertFalse(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchFalseUsuarioCreacionUsuarioReproduccionWithUrl() {
        InPutBeanDocument input = new InPutBeanDocument();
        input.setUsuarioCreacion("tevez http://twitter.com/messi");
        input.setUsuarioReproduccion("roman http://twitter.com/ronaldo");

        filterTexts.addAll(Arrays.asList("messi", "ronaldo"));

        Assert.assertFalse(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

}
