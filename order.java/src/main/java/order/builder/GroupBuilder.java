package order.builder;

import com.google.common.util.concurrent.ListeningExecutorService;
import order.Command;
import order.CommandPipeline;
import order.Executor;
import order.GroupCommand;
import order.sender.Sender;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Used to build a {@link GroupCommand}
 */
public class GroupBuilder<S extends Sender> {

    private String name, identifier, description;
    private Class<? extends Sender> senderClass = Command.DEFAULT_SENDER;

    private final ListeningExecutorService syncService;
    private final Executor<S> groupHelpExecutor;
    private final CommandPipeline<S> pipeline;

    @Inject
    public GroupBuilder(@Named("sync-executor") ListeningExecutorService syncService,
                        @Named("group-help-executor") Executor<S> groupHelpExecutor,
                        CommandPipeline<S> pipeline) {
        this.syncService = syncService;
        this.groupHelpExecutor = groupHelpExecutor;
        this.pipeline = pipeline;
    }

    public GroupBuilder<S> name(String name) {
        this.name = name;
        return this;
    }

    public GroupBuilder<S> identifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public GroupBuilder<S> description(String description) {
        this.description = description;
        return this;
    }

    public GroupBuilder<S> sender(Class<? extends Sender> senderClass) {
        this.senderClass = senderClass;
        return this;
    }

    public Executor<S> getExecutor() {
        return groupHelpExecutor;
    }

    public GroupCommand<S> build() {
        return new GroupCommand<S>(name, identifier, description, pipeline.build(groupHelpExecutor), pipeline
                .build(groupHelpExecutor), syncService, senderClass);
    }
}
