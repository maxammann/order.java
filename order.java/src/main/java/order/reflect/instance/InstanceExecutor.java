package order.reflect.instance;

import com.esotericsoftware.reflectasm.FieldAccess;
import order.CommandContext;
import order.ExecuteException;
import order.Executor;
import order.reflect.instance.factory.InstanceFactory;
import order.sender.Sender;

import javax.inject.Inject;
import java.lang.reflect.Field;

/**
 * Represents a InstanceExecutor
 */
public class InstanceExecutor<S extends Sender> implements Executor<S> {


    private final InstanceFactory factory;
    private final Class<? extends Executor<S>> clazz;
    private final FieldAccess access;

    @Inject
    public InstanceExecutor(InstanceFactory factory, Class<? extends Executor<S>> clazz) {
        this.factory = factory;
        this.clazz = clazz;

        this.access = FieldAccess.get(clazz);
    }

    @Override
    public void execute(CommandContext<S> ctx, S sender) throws ExecuteException {
        Executor<S> executor = factory.createInstance(clazz);


        for (Field field : clazz.getDeclaredFields()) {
            Object value = ctx.get(field.getName());

            if (value == null) {
                continue;
            }

            access.set(executor, field.getName(), value);
        }

        try {
            executor.execute(ctx, sender);
        } catch (ClassCastException e) {
            if (e.getMessage().contains("Sender")) {
                throw new ExecuteException("Incompatible sender!", ctx);
            }

            throw e;
        }

    }
}
