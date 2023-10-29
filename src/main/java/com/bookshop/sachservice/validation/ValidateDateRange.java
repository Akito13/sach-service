package com.bookshop.sachservice.validation;

import com.bookshop.sachservice.validation.validator.DateRangeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateDateRange {

    String message() default "Start time phải nằm trước End time";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
