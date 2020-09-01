package com.qurlapi.qurlapi.validation;

import com.qurlapi.qurlapi.dao.QUrlRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class StampExistingValidator implements ConstraintValidator<StampExists, String> {

    private final QUrlRepository qUrlRepository;
    private StampExists annotation;

    @Autowired
    public StampExistingValidator(final QUrlRepository qUrlRepository) {
        this.qUrlRepository = qUrlRepository;
    }

    @Override
    public void initialize(final StampExists constraintAnnotation) {
        annotation = constraintAnnotation;
    }

    public boolean isValid(final String stamp, final ConstraintValidatorContext context) {
        if (annotation.isPathVariable()) {
            return StringUtils.isNotBlank(stamp) && qUrlRepository.findByStamp(stamp).isPresent();
        } else {
            return StringUtils.isBlank(stamp) || qUrlRepository.findByStamp(stamp).isEmpty();
        }
    }
}
