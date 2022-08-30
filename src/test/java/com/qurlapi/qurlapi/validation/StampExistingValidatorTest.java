package com.qurlapi.qurlapi.validation;

import com.qurlapi.qurlapi.assembler.dto.QUrlRequestAssembler;
import com.qurlapi.qurlapi.assembler.model.QUrlAssembler;
import com.qurlapi.qurlapi.dao.QUrlRepository;
import com.qurlapi.qurlapi.dto.request.QUrlRequest;
import org.hibernate.validator.internal.constraintvalidators.bv.NotBlankValidator;
import org.hibernate.validator.internal.constraintvalidators.bv.NotNullValidator;
import org.hibernate.validator.internal.constraintvalidators.bv.number.bound.MaxValidatorForInteger;
import org.hibernate.validator.internal.constraintvalidators.bv.number.bound.MinValidatorForInteger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintValidatorFactory;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class StampExistingValidatorTest {

    private static Validator validator;
    private static QUrlRepository qUrlRepository;

    @BeforeAll
    public static void setUp() {
        qUrlRepository = mock(QUrlRepository.class);

        final ConstraintValidatorFactory cvf = mock(ConstraintValidatorFactory.class);
        when(cvf.getInstance(StampExistingValidator.class)).thenReturn(new StampExistingValidator(qUrlRepository));
        when(cvf.getInstance(NotBlankValidator.class)).thenReturn(new NotBlankValidator());
        when(cvf.getInstance(MinValidatorForInteger.class)).thenReturn(new MinValidatorForInteger());
        when(cvf.getInstance(MaxValidatorForInteger.class)).thenReturn(new MaxValidatorForInteger());
        when(cvf.getInstance(NotNullValidator.class)).thenReturn(new NotNullValidator());
        validator = Validation.buildDefaultValidatorFactory()
                              .usingContext()
                              .constraintValidatorFactory(cvf)
                              .getValidator();
    }

    @Test
    public void shouldReturnNoViolationsWhenStampDoesNotExists() {
        // given
        final QUrlRequest request = QUrlRequestAssembler.any();
        when(qUrlRepository.findByStamp(eq(request.getStamp()))).thenReturn(Optional.empty());

        // when
        final Set<ConstraintViolation<QUrlRequest>> constraintViolations = validator.validate(request);

        // then
        final int expectedViolations = 0;
        assertEquals(expectedViolations, constraintViolations.size());
    }

    @Test
    public void shouldReturnOneViolationsWhenStampIsTaken() {
        // given
        final QUrlRequest request = QUrlRequestAssembler.any();
        when(qUrlRepository.findByStamp(eq(request.getStamp()))).thenReturn(Optional.of(QUrlAssembler.any()));

        // when
        final Set<ConstraintViolation<QUrlRequest>> constraintViolations = validator.validate(request);

        // then
        final int expectedViolations = 1;
        final String expectedMessage = "Stamp taken!";

        assertEquals(expectedViolations, constraintViolations.size());
        constraintViolations.stream()
                            .findFirst()
                            .ifPresent((violation) -> assertEquals(expectedMessage, violation.getMessage()));
    }
}
