package order.reflect.instance.factory;

/**
 * Represents a SimpleInstanceFactory
 */
public class SimpleInstanceFactory implements InstanceFactory {


    @Override
    public <T> T createInstance(Class<? extends T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

