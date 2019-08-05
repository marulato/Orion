package org.orion.common.validation.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Length {
    int min() default 0;
    int max() default 0;
    String errorCode();
}
