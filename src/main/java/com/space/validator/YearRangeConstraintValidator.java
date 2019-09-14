package com.space.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;
import java.util.Date;

public class YearRangeConstraintValidator implements ConstraintValidator<YearRange, Date> {

    private YearRange annotationYearRange;

    @Override
    public void initialize(YearRange range) {
        this.annotationYearRange = range;
    }

    @Override
    public boolean isValid(Date target, ConstraintValidatorContext cxt) {
        if(target == null) {
            return true;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(target);
        int targetYear = c.get(Calendar.YEAR);

        return targetYear >= annotationYearRange.min() && targetYear <= annotationYearRange.max();
    }

}
