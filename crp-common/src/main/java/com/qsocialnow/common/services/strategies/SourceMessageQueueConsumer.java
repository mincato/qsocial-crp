package com.qsocialnow.common.services.strategies;

import com.qsocialnow.common.queues.QueueConsumer;

public class SourceMessageQueueConsumer extends QueueConsumer<SourceMessageRequest> {

    private AsyncTask<SourceMessageRequest, SourceMessageResponse> sourceStrategy;

    private SourceMessagePostProcess postProcess;

    public SourceMessageQueueConsumer(String type,
            AsyncTask<SourceMessageRequest, SourceMessageResponse> sourceStrategy, SourceMessagePostProcess postProcess) {
        super(type);
        this.sourceStrategy = sourceStrategy;
        this.postProcess = postProcess;
    }

    @Override
    public void process(SourceMessageRequest document) {
        SourceMessageResponse response = sourceStrategy.execute(document);
        if (response != null && postProcess != null) {
            postProcess.process(response);
        }

    }

    @Override
    public void save() {
        // TODO Auto-generated method stub

    }

}
