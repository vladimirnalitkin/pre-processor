package com.van.processor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface RestControllerOnItem {
        boolean exported() default true;

        String path() default "";

        String collectionResourceRel() default "";

        String itemResourceRel() default "";

        boolean security() default false;

}