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
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.mock.CaseBuilder;
import com.qsocialnow.persistence.CaseRepository;
import com.qsocialnow.persistence.TeamRepository;

@RunWith(MockitoJUnitRunner.class)
public class CaseServiceTest {

    @Mock
    private CaseRepository caseRepository;

    @Mock
    private TeamRepository teamRepository;

    private CaseService caseService;

    private CaseListView case1ListView;
    private CaseListView case2ListView;
    private CaseListView case3ListView;

    @Before
    public void init() {
        caseService = new CaseService();
        caseService.setRepository(caseRepository);
        caseService.setTeamRepository(teamRepository);
        case1ListView = CaseBuilder.createCase1ListView();
        case2ListView = CaseBuilder.createCase2ListView();
        case3ListView = CaseBuilder.createCase3ListView();
    }

    @Test
    public void find() {
        List<CaseListView> expectedCases = Arrays.asList(case1ListView, case2ListView, case3ListView);

        PageResponse<CaseListView> expectedPageCases = new PageResponse<CaseListView>(expectedCases, 0, 0);
        when(teamRepository.findTeams(Mockito.matches("bruceWayne"))).thenReturn(null);

        when(
                caseRepository.findAll(Mockito.any(PageRequest.class), Mockito.matches("domainId"),
                        Mockito.matches("triggerId"), Mockito.matches("segmentId"), Mockito.matches("subject"),
                        Mockito.matches("title"), Mockito.matches("true"), Mockito.matches("priority"),
                        Mockito.matches("true"), Mockito.matches("1476719187665"), Mockito.matches("1476719187665"),
                        Mockito.anyList(), Mockito.matches("bruceWayne"), Mockito.matches("userSelected"),
                        Mockito.matches("caseCategory"), Mockito.matches("subjectCategory"))).thenReturn(expectedCases);

        PageResponse<CaseListView> results = caseService.findAll(0, 0, "title", "true", "domainId", "triggerId",
                "segmentId", "subject", "title", "true", "priority", "true", "1476719187665", "1476719187665",
                "bruceWayne", "userSelected", "caseCategory", "subjectCategory");
        ReflectionAssert.assertReflectionEquals(expectedPageCases, results);
    }
}
