package order.reflect.instance.factory;


import com.google.inject.Injector;

import javax.inject.Inject;

/**
 * Represents a InjectorInstanceFactory
 */
public class InjectorInstanceFactory implements InstanceFactory {

    private final Injector injector;

    @Inject
    public InjectorInstanceFactory(Injector injector) {
        this.injector = injector;
    }

    @Override
    public <T> T createInstance(Class<? extends T> clazz) {
        return injector.getInstance(clazz);
    }
}
