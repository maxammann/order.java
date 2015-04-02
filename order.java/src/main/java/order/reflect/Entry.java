package order.reflect;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 */
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Entry {

    String DEFAULT_VALUE = "entry-default45617654115";

    String key();

    String value() default DEFAULT_VALUE;

}
