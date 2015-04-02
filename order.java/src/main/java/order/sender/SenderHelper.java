package order.sender;

/**
 * Represents a SenderHelper
 */
public class SenderHelper {

    public static <S extends Sender, R> R as(Sender.Executor<S, R> executor, Class<S> clazz, Sender test) {
        if (clazz.isAssignableFrom(test.getClass())) {
            S sender = (S) test;
            return executor.execute(sender);
        }

        return executor.defaultValue(test);
    }
}
