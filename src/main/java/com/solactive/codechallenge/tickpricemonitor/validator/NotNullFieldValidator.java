package com.solactive.codechallenge.tickpricemonitor.validator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @author Ramesh.Yaleru on 4/29/2021
 */
@Slf4j
public class NotNullFieldValidator implements ConstraintValidator<NotNullField, Object> {

    private String fieldName;
    private String condition;
    private String message;

    @Override
    public void initialize(NotNullField constraintAnnotation) {
        fieldName = constraintAnnotation.fieldName();
        condition = constraintAnnotation.condition();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object request, ConstraintValidatorContext context) {
        Boolean flag = false;
        try {
            ExpressionParser parser = new SpelExpressionParser();
            flag = parser.parseExpression(condition).getValue(request, Boolean.class);
        } catch (Exception e) {
            log.error("Error while parsing spring expression during custom not null validation.", e);
            return true;
        }
        if (!BooleanUtils.toBoolean(flag)) {
            log.info("Validation not applied on field {} as condition {} evaluated as false.", fieldName, condition);
            return true;
        }
        try {
            Object value = BeanUtils.getProperty(request, fieldName);
            if (Objects.isNull(value)) {
                if (Objects.isNull(message)) {
                    message = fieldName + " must not be null or empty.";
                }
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message).addPropertyNode(fieldName).addConstraintViolation();
                return false;
            }
        } catch (Exception e) {
            log.error("Exception while field validation with field name : {}.", fieldName, e);
        }
        return true;
    }
}
