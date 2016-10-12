package com.qsocialnow.eventresolver.processor;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.config.AutomaticActionCriteria;
import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.event.Event;
import com.qsocialnow.eventresolver.action.Action;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class ExecutionMessageProcessorTest {

    @Mock
    private Action action;

    private ExecutionMessageProcessor executionMessageProcessor;

    public ExecutionMessageProcessorTest() {
        executionMessageProcessor = new ExecutionMessageProcessor();
        executionMessageProcessor.setUpsertCaseStrategySelector(new UpsertCaseStrategySelector());
    }

    @Before
    public void setup() {
        HashMap<ActionType, Action> actions = new HashMap<>();
        actions.put(ActionType.OPEN_CASE, action);
        executionMessageProcessor.setActions(actions);
    }

    @Test
    public void testExecuteNull() {
        executionMessageProcessor.execute(null);
    }

    @Test
    public void testExecuteInputNull() {
        ExecutionMessageRequest request = new ExecutionMessageRequest(null, null, new DetectionCriteria(), null, null);
        executionMessageProcessor.execute(request);
    }

    @Test
    public void testExecuteDetectionCriteriaNull() {
        ExecutionMessageRequest request = new ExecutionMessageRequest(new Event(), null, null, null, null);
        executionMessageProcessor.execute(request);
    }

    @Test
    public void testExecuteAutomaticActionCriteriaDomainNull() {
        ExecutionMessageRequest request = new ExecutionMessageRequest(new Event(), null, new DetectionCriteria(), null,
                null);
        executionMessageProcessor.execute(request);
    }

    @Test
    public void testExecuteAutomaticActionCriteriaDomain() {
        ExecutionMessageRequest request = new ExecutionMessageRequest(new Event(), new Domain(),
                new DetectionCriteria(), null, null);
        executionMessageProcessor.execute(request);
    }

    @Test
    public void testExecuteOneAction() {

        DetectionCriteria detectionCriteria = new DetectionCriteria();
        ArrayList<AutomaticActionCriteria> automaticActions = new ArrayList<>();
        AutomaticActionCriteria automaticActionCriteria = new AutomaticActionCriteria();
        automaticActionCriteria.setActionType(ActionType.OPEN_CASE);
        automaticActions.add(automaticActionCriteria);
        detectionCriteria.setActionCriterias(automaticActions);

        ExecutionMessageRequest request = new ExecutionMessageRequest(new Event(), new Domain(), detectionCriteria,
                null, null);
        executionMessageProcessor.execute(request);

        Mockito.verify(action, Mockito.times(1)).execute(Mockito.any(),
                Mockito.anyMapOf(ActionParameter.class, Object.class), Mockito.any(ExecutionMessageRequest.class));
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
        detectionCriteria.setActionCriterias(automaticActions);

        ExecutionMessageRequest request = new ExecutionMessageRequest(new Event(), new Domain(), detectionCriteria,
                null, null);
        executionMessageProcessor.execute(request);

        Mockito.verify(action, Mockito.times(2)).execute(Mockito.any(),
                Mockito.anyMapOf(ActionParameter.class, Object.class), Mockito.any(ExecutionMessageRequest.class));
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
        detectionCriteria.setActionCriterias(automaticActions);
        ExecutionMessageRequest request = new ExecutionMessageRequest(new Event(), new Domain(), detectionCriteria,
                null, null);

        executionMessageProcessor.execute(request);

        Mockito.verify(action, Mockito.times(1)).execute(Mockito.any(),
                Mockito.anyMapOf(ActionParameter.class, Object.class), Mockito.any(ExecutionMessageRequest.class));
    }

}
