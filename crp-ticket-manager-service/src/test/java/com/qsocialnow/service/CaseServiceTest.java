package com.qsocialnow.service;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.unitils.reflectionassert.ReflectionAssert;

import com.qsocialnow.common.model.cases.CaseListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.mock.CaseBuilder;
import com.qsocialnow.persistence.CaseRepository;

@RunWith(MockitoJUnitRunner.class)
public class CaseServiceTest {

    @Mock
    private CaseRepository caseRepository;

    private CaseService caseService;

    private CaseListView case1ListView;
    private CaseListView case2ListView;
    private CaseListView case3ListView;

    @Before
    public void init() {
        caseService = new CaseService();
        caseService.setRepository(caseRepository);
        case1ListView = CaseBuilder.createCase1ListView();
        case2ListView = CaseBuilder.createCase2ListView();
        case3ListView = CaseBuilder.createCase3ListView();
    }

    @Test
    public void find() {
        List<CaseListView> expectedCases = Arrays.asList(case1ListView, case2ListView, case3ListView);

        PageResponse<CaseListView> expectedPageCases = new PageResponse<CaseListView>(expectedCases, 0, 0);
        when(caseRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(expectedCases);

        PageResponse<CaseListView> results = caseService.findAll(0, 0);

        ReflectionAssert.assertReflectionEquals(expectedPageCases, results);
    }
}
