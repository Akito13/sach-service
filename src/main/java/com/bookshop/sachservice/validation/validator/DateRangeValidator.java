package com.bookshop.sachservice.validation.validator;

import com.bookshop.sachservice.dto.TimeRange;
import com.bookshop.sachservice.validation.ValidateDateRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<ValidateDateRange, TimeRange> {
    @Override
    public void initialize(ValidateDateRange constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(TimeRange giaDto, ConstraintValidatorContext constraintValidatorContext) {
        if(giaDto == null || (giaDto.getStartTime() == null && giaDto.getEndTime() == null))
            return true;
        return giaDto.getStartTime().isBefore(giaDto.getEndTime());
    }
}
