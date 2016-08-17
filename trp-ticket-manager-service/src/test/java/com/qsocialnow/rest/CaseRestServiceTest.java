package com.qsocialnow.rest;

import java.util.Arrays;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.qsocialnow.common.model.cases.CaseListView;
import com.qsocialnow.common.pagination.PageResponse;
import com.qsocialnow.rest.response.RestResponseHandler;
import com.qsocialnow.service.CaseService;

@RunWith(MockitoJUnitRunner.class)
public class CaseRestServiceTest {

    private CaseRestService service;

    @Mock
    private CaseService caseService;

    @Before
    public void init() {
        service = new CaseRestService();
        service.setResponseHandler(new RestResponseHandler());
        service.setCaseService(caseService);
    }

    @Test
    public void find() {
        PageResponse<CaseListView> cases = new PageResponse<CaseListView>(Arrays.asList(new CaseListView(),
                new CaseListView(), new CaseListView()), 0, 0, 0l);
        Mockito.when(caseService.findAll(Mockito.any(Integer.class), Mockito.any(Integer.class))).thenReturn(cases);

        Response response = service.findAll(null, null, null);

        Mockito.verify(caseService, Mockito.times(1)).findAll(null, null);
        Assert.assertEquals(cases, response.getEntity());
    }

}
