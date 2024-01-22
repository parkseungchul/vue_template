package com.example.backend.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @interface is used to define an annotation type
 * @Retention in Java determines how long an annotation is kept 'SOURCE', 'CLASS', 'RUNTIME'.
 * @Target in Java specifies where an annotation can be applied, such as TYPE, FIELD, METHOD, PARAMETER,
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthenticatedEndpoint {

    /**
     * This is the option for "@Authentication annotation'
     * @return
     */
    boolean throwOnUnauthorized() default false;

}
