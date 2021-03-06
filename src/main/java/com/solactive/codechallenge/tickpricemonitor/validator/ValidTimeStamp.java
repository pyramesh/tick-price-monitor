package com.solactive.codechallenge.tickpricemonitor.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Ramesh.Yaleru on 4/30/2021
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTimeStamp {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
