package order;


import order.sender.Sender;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Represents a PipelinedProvider
 */
public class PipelinedProvider<S extends Sender, E extends Executor<S>> implements Provider<Executor<S>> {


    private final CommandPipeline<S> pipeline;
    private final E executor;

    @Inject
    public PipelinedProvider(CommandPipeline<S> pipeline, E executor) {
        this.pipeline = pipeline;
        this.executor = executor;
    }

    @Override
    public Executor<S> get() {
        return pipeline.build(executor);
    }
}
