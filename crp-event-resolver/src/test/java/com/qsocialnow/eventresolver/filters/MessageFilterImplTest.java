package com.qsocialnow.eventresolver.filters;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.event.InPutBeanDocument;

public class MessageFilterImplTest {

    private MessageFilterImpl messageFilter = new MessageFilterImpl();

    @Test
    public void testShouldProcessDomainNull() {
        Assert.assertFalse(messageFilter.shouldProcess(null, null));
    }

    @Test
    public void testShouldProcessThematicsNull() {
        Assert.assertFalse(messageFilter.shouldProcess(null, new Domain()));
    }

    @Test
    public void testShouldProcessThematicsEmpty() {
        Domain domain = new Domain();
        domain.setThematics(new ArrayList<>());
        Assert.assertFalse(messageFilter.shouldProcess(null, domain));
    }

    @Test
    public void testShouldProcessTrue() {
        Domain domain = new Domain();
        domain.setThematics(Arrays.asList(1L, 2L));

        InPutBeanDocument message = new InPutBeanDocument();
        message.setTokenId(1L);

        Assert.assertTrue(messageFilter.shouldProcess(message, domain));
    }

    @Test
    public void testShouldProcessFalse() {
        Domain domain = new Domain();
        domain.setThematics(Arrays.asList(1L, 2L));

        InPutBeanDocument message = new InPutBeanDocument();
        message.setTokenId(3L);

        Assert.assertFalse(messageFilter.shouldProcess(message, domain));
    }

}
