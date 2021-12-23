package com.hell.annotation.validation;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class UserIdValidator implements ConstraintValidator<UserId, String> {
    private String msg;
    private String regexp;
    @Override
    public void initialize(UserId constraintAnnotation) {
        this.msg = constraintAnnotation.message();
        this.regexp = constraintAnnotation.regexp();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if(StringUtils.isEmpty(s)) {
            return false;
        }
        if(Pattern.matches(this.regexp, s)) {
            return true;
        }

        if (context.getDefaultConstraintMessageTemplate().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(this.msg).addConstraintViolation();
        }
        return false;
    }
}
