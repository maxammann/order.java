package order;

import order.sender.Sender;

/**
 * Represents a CommandPipeline
 */
// Build locations: GroupBuilder, CommandAnalyser, Commands
public interface CommandPipeline<S extends Sender> {

    void addAfter(Executor<S> executor);

    void addBefore(Executor<S> executor);

    Executor<S> build(Executor<S> executor);
}
