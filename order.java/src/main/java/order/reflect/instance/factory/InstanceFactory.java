package order.reflect.instance.factory;

/**
 * Represents a InstanceFactory
 */
public interface InstanceFactory {

    <T> T createInstance(Class<? extends T> clazz);
}
