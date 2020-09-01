package com.qurlapi.qurlapi.validation;

import com.qurlapi.qurlapi.dto.request.QUrlRequest;
import com.qurlapi.qurlapi.service.QUrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
public class StampExistingValidatorTest {

    @Autowired
    private QUrlService qUrlService;
    @Autowired
    private Validator validator;

    @BeforeEach
    public void beforeEach() {
        qUrlService.purge();
    }

    @Test
    public void shouldReturnNoViolationsWhenStampDoesNotExists() {
        // given
        final String url = "http://example.com";
        final String stamp = "test";
        final int usages = 3;

        final QUrlRequest request = QUrlRequest.builder()
                .url(url)
                .stamp(stamp)
                .usages(usages)
                .build();

        // when
        final Set<ConstraintViolation<QUrlRequest>> constraintViolations = validator.validate(request);

        // then
        final int expectedViolations = 0;
        assertEquals(expectedViolations, constraintViolations.size());
    }

    @Test
    public void shouldReturnOneViolationsWhenStampIsTaken() {
        // given
        final String url = "http://example.com";
        final String stamp = "test";
        final int usages = 3;

        final QUrlRequest request = QUrlRequest.builder()
                .url(url)
                .stamp(stamp)
                .usages(usages)
                .build();

        qUrlService.addQUrl(request);

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
