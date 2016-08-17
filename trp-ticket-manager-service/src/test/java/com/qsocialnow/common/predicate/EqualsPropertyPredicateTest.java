package com.qsocialnow.common.predicate;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;

public class EqualsPropertyPredicateTest {

    @Test
    public void testSimpleFind() {

        List<Foo> foos = Arrays.asList(new Foo(null, "one"), new Foo(13L, "two"), new Foo(1234L, "three"));

        Foo foo = (Foo) CollectionUtils.find(foos, new EqualsPropertyPredicate("id", 1234L));

        Assert.assertNotNull(foo);
        Assert.assertEquals(new Long(1234), foo.getId());
        Assert.assertEquals("three", foo.getAttr());
    }

    @Test
    public void testHierarchicalFind() {

        List<Bar> bars = Arrays.asList(new Bar(null, "one", "ONE"), new Bar(13L, "two", "TWO"), new Bar(1234L, "three",
                "THREE"));

        Bar bar = (Bar) CollectionUtils.find(bars, new EqualsPropertyPredicate("id", 1234L));

        Assert.assertNotNull(bar);
        Assert.assertEquals(new Long(1234), bar.getId());
        Assert.assertEquals("three", bar.getAttr());
    }

    @Test
    public void testNonexistentAttribute() {

        List<Foo> foos = Arrays.asList(new Foo(null, "one"), new Foo(13L, "two"), new Foo(1234L, "three"));

        Foo foo = (Foo) CollectionUtils.find(foos, new EqualsPropertyPredicate("ipso", 1234L));

        Assert.assertNull(foo);
    }

}
