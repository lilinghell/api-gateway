package com.hell.annotation.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = UserIdValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserId {
    String message() default "javax.validation.constraints.UserId.message";

    String regexp() default "^\\w*[a-zA-Z]+\\w*$";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

