package com.qsocialnow.eventresolver.filters;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.qsocialnow.common.model.config.CategoryFilter;
import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.common.model.event.Event;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

public class CategoryDetectionCriteriaFilterTest {

    private CategoryDetectionCriteriaFilter filter;

    private Filter filterConfig;

    public CategoryDetectionCriteriaFilterTest() {
        filter = new CategoryDetectionCriteriaFilter();
        filterConfig = new Filter();
    }

    @Test
    public void testApplyTrue() {
        CategoryFilter categoryFilter = new CategoryFilter();
        categoryFilter.setCategoryGroup(1l);
        filterConfig.setCategoryFilter(Arrays.asList(categoryFilter));

        Assert.assertTrue(filter.apply(filterConfig));
    }

    @Test
    public void testApplyFalseNullCategoryFilter() {
        Assert.assertFalse(filter.apply(filterConfig));
    }

    @Test
    public void testApplyFalseEmptyCategoryFilters() {
        filterConfig.setCategoryFilter(new ArrayList<>());

        Assert.assertFalse(filter.apply(filterConfig));
    }

    @Test
    public void testApplyFalseNullCategoryGroup() {
        CategoryFilter categoryFilter = new CategoryFilter();
        filterConfig.setCategoryFilter(Arrays.asList(categoryFilter));

        Assert.assertFalse(filter.apply(filterConfig));
    }

    @Test
    public void testMatchInputNull() {
        Event input = new Event();

        CategoryFilter categoryFilter = new CategoryFilter();
        categoryFilter.setCategoryGroup(1l);
        filterConfig.setCategoryFilter(Arrays.asList(categoryFilter));

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchTrueOnlyCategoryGroup() {
        Event input = new Event();
        input.setConjuntos(new Long[] { 1l });
        input.setCategorias(new Long[] { 2l, 3l });

        CategoryFilter categoryFilter = new CategoryFilter();
        categoryFilter.setCategoryGroup(1l);
        filterConfig.setCategoryFilter(Arrays.asList(categoryFilter));

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchTrueCategoryGroupAndCategorias() {
        Event input = new Event();
        input.setConjuntos(new Long[] { 1l });
        input.setCategorias(new Long[] { 2l, 3l });

        CategoryFilter categoryFilter = new CategoryFilter();
        categoryFilter.setCategoryGroup(1l);
        categoryFilter.setCategories(new Long[] { 2l });
        filterConfig.setCategoryFilter(Arrays.asList(categoryFilter));

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchTrueCategoryGroupAndCategoriasMultipleValues() {
        Event input = new Event();
        input.setConjuntos(new Long[] { 1l });
        input.setCategorias(new Long[] { 2l, 3l });

        CategoryFilter categoryFilter = new CategoryFilter();
        categoryFilter.setCategoryGroup(1l);
        categoryFilter.setCategories(new Long[] { 2l, 3l });
        filterConfig.setCategoryFilter(Arrays.asList(categoryFilter));

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchTrueMultipleFiltersCategoryGroupAndCategoriasMultipleValuesSecondTrue() {
        Event input = new Event();
        input.setConjuntos(new Long[] { 1l });
        input.setCategorias(new Long[] { 2l, 3l });

        CategoryFilter categoryFilter = new CategoryFilter();
        categoryFilter.setCategoryGroup(2l);
        categoryFilter.setCategories(new Long[] { 3l, 4l });
        CategoryFilter categoryFilter1 = new CategoryFilter();
        categoryFilter1.setCategoryGroup(1l);
        categoryFilter1.setCategories(new Long[] { 2l, 3l });
        filterConfig.setCategoryFilter(Arrays.asList(categoryFilter, categoryFilter1));

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchTrueMultipleFiltersCategoryGroupAndCategoriasMultipleValuesFirstTrue() {
        Event input = new Event();
        input.setConjuntos(new Long[] { 1l });
        input.setCategorias(new Long[] { 2l, 3l });

        CategoryFilter categoryFilter = new CategoryFilter();
        categoryFilter.setCategoryGroup(1l);
        categoryFilter.setCategories(new Long[] { 2l });
        CategoryFilter categoryFilter1 = new CategoryFilter();
        categoryFilter1.setCategoryGroup(1l);
        categoryFilter1.setCategories(new Long[] { 3l, 4l });
        filterConfig.setCategoryFilter(Arrays.asList(categoryFilter, categoryFilter1));

        Assert.assertTrue(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseOnlyCategoryGroup() {
        Event input = new Event();
        input.setConjuntos(new Long[] { 1l });
        input.setCategorias(new Long[] { 2l, 3l });

        CategoryFilter categoryFilter = new CategoryFilter();
        categoryFilter.setCategoryGroup(2l);
        filterConfig.setCategoryFilter(Arrays.asList(categoryFilter));

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseCategoryGroupAndCategorias() {
        Event input = new Event();
        input.setConjuntos(new Long[] { 1l });
        input.setCategorias(new Long[] { 2l, 3l });

        CategoryFilter categoryFilter = new CategoryFilter();
        categoryFilter.setCategoryGroup(1l);
        categoryFilter.setCategories(new Long[] { 4l });
        filterConfig.setCategoryFilter(Arrays.asList(categoryFilter));

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseCategoryGroupAndCategoriasMultipleValues() {
        Event input = new Event();
        input.setConjuntos(new Long[] { 1l });
        input.setCategorias(new Long[] { 2l, 3l });

        CategoryFilter categoryFilter = new CategoryFilter();
        categoryFilter.setCategoryGroup(1l);
        categoryFilter.setCategories(new Long[] { 2l, 4l });
        filterConfig.setCategoryFilter(Arrays.asList(categoryFilter));

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

    @Test
    public void testMatchFalseMultipleFiltersCategoryGroupAndCategoriasMultipleValues() {
        Event input = new Event();
        input.setConjuntos(new Long[] { 1l });
        input.setCategorias(new Long[] { 2l, 3l });

        CategoryFilter categoryFilter = new CategoryFilter();
        categoryFilter.setCategoryGroup(1l);
        categoryFilter.setCategories(new Long[] { 2l, 4l });
        CategoryFilter categoryFilter1 = new CategoryFilter();
        categoryFilter1.setCategoryGroup(2l);
        categoryFilter1.setCategories(new Long[] { 2l, 3l });
        filterConfig.setCategoryFilter(Arrays.asList(categoryFilter, categoryFilter1));

        Assert.assertFalse(filter.match(new NormalizedInputBeanDocument(input), filterConfig));
    }

}
