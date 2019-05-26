package org.orion.common.validation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidateWithMethod {
    String[] methodName();
    String[] errorCode();
}
