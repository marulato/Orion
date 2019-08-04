package org.orion.common.dao.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SearchColumn {
    String pattern() default "like";
    String prefix() default "%";
    String suffix() default "%";
}
