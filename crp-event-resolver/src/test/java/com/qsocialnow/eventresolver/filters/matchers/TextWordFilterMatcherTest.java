package com.qsocialnow.eventresolver.filters.matchers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.qsocialnow.common.model.event.Event;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;
import com.qsocialnow.eventresolver.normalizer.TextNormalizer;

public class TextWordFilterMatcherTest {

    private TextWordFilterMatcher matcher;
    private Set<String> filterTexts;

    public TextWordFilterMatcherTest() {
        matcher = new TextWordFilterMatcher();
        matcher.setTextNormalizer(new TextNormalizer());
        filterTexts = new HashSet<>();
    }

    @Test
    public void testMatchInputNull() {
        Event input = new Event();

        filterTexts.addAll(Arrays.asList("messi", "ronaldo"));

        Assert.assertFalse(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchTrue() {
        Event input = new Event();
        input.setTexto("el genio de messi le gano a ronaldo gracias a tevez");

        filterTexts.addAll(Arrays.asList("messi"));

        Assert.assertTrue(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchTrueMultipleValues() {
        Event input = new Event();
        input.setTexto("el genio de messi le gano a ronaldo gracias a tevez");

        filterTexts.addAll(Arrays.asList("messi", "ronaldo"));

        Assert.assertTrue(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchTrueUpperCase() {
        Event input = new Event();
        input.setTexto("El Genio de MESSI le ganó a Ronaldo gracias a Tevez");

        filterTexts.addAll(Arrays.asList("messi"));

        Assert.assertTrue(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchTrueMultipleValuesUpperCase() {
        Event input = new Event();
        input.setTexto("El Genio de MESSI le ganó a Ronaldo gracias a Tevez");

        filterTexts.addAll(Arrays.asList("messi", "gano", "ronaldo"));

        Assert.assertTrue(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchFalse() {
        Event input = new Event();
        input.setTexto("el genio de messi le gano a ronaldo gracias a tevez");

        filterTexts.addAll(Arrays.asList("roman"));

        Assert.assertFalse(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchFalseMultipleValues() {
        Event input = new Event();
        input.setTexto("el genio de messi le gano a ronaldo gracias a tevez");

        filterTexts.addAll(Arrays.asList("messi", "roman"));

        Assert.assertFalse(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }

    @Test
    public void testMatchFalseMultipleValuesWithStopWord() {
        Event input = new Event();
        input.setTexto("el genio de messi le gano a ronaldo gracias a tevez");

        filterTexts.addAll(Arrays.asList("messi", "ronaldo", "el"));

        Assert.assertFalse(matcher.match(new NormalizedInputBeanDocument(input), filterTexts));
    }
}
