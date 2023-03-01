package com.van.processor.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
@Inherited
public @interface CalculatedColumn {
    String value() default "";
}
