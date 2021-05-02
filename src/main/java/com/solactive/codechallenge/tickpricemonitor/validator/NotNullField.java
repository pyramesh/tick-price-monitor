package com.solactive.codechallenge.tickpricemonitor.validator;

import org.springframework.http.HttpStatus;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Ramesh.Yaleru on 4/29/2021
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotNullFieldValidator.class)
public @interface NotNullField {

    String fieldName();

    String message() default "field must not be null or empty";

    String condition() default "true";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        NotNullField[] value();
    }
}
