package org.orion.common.validation.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Regex {
    String pattern() default "matches";
    String regex();
    String errorCode();
}
