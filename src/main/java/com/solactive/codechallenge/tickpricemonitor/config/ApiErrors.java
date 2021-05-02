package com.solactive.codechallenge.tickpricemonitor.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Ramesh.Yaleru on 5/2/2021
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiErrors {
    ApiError[] value();
}
