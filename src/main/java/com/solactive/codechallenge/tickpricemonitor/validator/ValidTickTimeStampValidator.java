package com.solactive.codechallenge.tickpricemonitor.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Ramesh.Yaleru on 4/30/2021
 */
@Component
public class ValidTickTimeStampValidator {

    @Value("${tick.sliding.time.window}")
    private long slidingTimeInterval;

    public boolean isValid(long tickTimestamp) {
        return  tickTimestamp >= (System.currentTimeMillis() - slidingTimeInterval);
    }
}
