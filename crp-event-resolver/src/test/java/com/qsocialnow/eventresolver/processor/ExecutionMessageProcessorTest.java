package com.qsocialnow.eventresolver.processor;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.qsocial.eventresolver.model.event.InPutBeanDocument;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.config.AutomaticActionCriteria;
import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.eventresolver.action.Action;

@RunWith(MockitoJUnitRunner.class)
public class ExecutionMessageProcessorTest {

    @Mock
    private Action<Object, Object> action;

    private ExecutionMessageProcessor executionMessageProcessor;

    public ExecutionMessageProcessorTest() {
        executionMessageProcessor = new ExecutionMessageProcessor();
    }

    @Before
    public void setup() {
        HashMap<ActionType, Action> actions = new HashMap<>();
        actions.put(ActionType.OPEN_CASE, action);
        executionMessageProcessor.setActions(actions);
    }

    @Test
    public void testExecuteNull() {
        executionMessageProcessor.execute(null, null);
    }

    @Test
    public void testExecuteInputNull() {
        executionMessageProcessor.execute(null, new DetectionCriteria());
    }

    @Test
    public void testExecuteDetectionCriteriaNull() {
        executionMessageProcessor.execute(new InPutBeanDocument(), null);
    }

    @Test
    public void testExecuteAutomaticActionCriteriaNull() {
        executionMessageProcessor.execute(new InPutBeanDocument(), new DetectionCriteria());
    }

    @Test
    public void testExecuteOneAction() {

        DetectionCriteria detectionCriteria = new DetectionCriteria();
        ArrayList<AutomaticActionCriteria> automaticActions = new ArrayList<>();
        AutomaticActionCriteria automaticActionCriteria = new AutomaticActionCriteria();
        automaticActionCriteria.setActionType(ActionType.OPEN_CASE);
        automaticActions.add(automaticActionCriteria);
        detectionCriteria.setAccionCriterias(automaticActions);

        executionMessageProcessor.execute(new InPutBeanDocument(), detectionCriteria);

        Mockito.verify(action, Mockito.times(1)).execute(Mockito.any(), Mockito.anyListOf(String.class));
    }

    @Test
    public void testExecuteTwoActions() {

        DetectionCriteria detectionCriteria = new DetectionCriteria();
        ArrayList<AutomaticActionCriteria> automaticActions = new ArrayList<>();
        AutomaticActionCriteria automaticActionCriteria = new AutomaticActionCriteria();
        automaticActionCriteria.setActionType(ActionType.OPEN_CASE);
        automaticActions.add(automaticActionCriteria);
        automaticActionCriteria = new AutomaticActionCriteria();
        automaticActionCriteria.setActionType(ActionType.OPEN_CASE);
        automaticActions.add(automaticActionCriteria);
        detectionCriteria.setAccionCriterias(automaticActions);

        executionMessageProcessor.execute(new InPutBeanDocument(), detectionCriteria);

        Mockito.verify(action, Mockito.times(2)).execute(Mockito.any(), Mockito.anyListOf(String.class));
    }

    @Test
    public void testExecuteOneActionOfTwoAcions() {

        DetectionCriteria detectionCriteria = new DetectionCriteria();
        ArrayList<AutomaticActionCriteria> automaticActions = new ArrayList<>();
        AutomaticActionCriteria automaticActionCriteria = new AutomaticActionCriteria();
        automaticActionCriteria.setActionType(ActionType.OPEN_CASE);
        automaticActions.add(automaticActionCriteria);
        automaticActionCriteria = new AutomaticActionCriteria();
        automaticActionCriteria.setActionType(ActionType.CLOSE);
        automaticActions.add(automaticActionCriteria);
        detectionCriteria.setAccionCriterias(automaticActions);

        executionMessageProcessor.execute(new InPutBeanDocument(), detectionCriteria);

        Mockito.verify(action, Mockito.times(1)).execute(Mockito.any(), Mockito.anyListOf(String.class));
    }

}
