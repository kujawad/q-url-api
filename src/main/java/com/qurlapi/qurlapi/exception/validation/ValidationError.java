package com.qurlapi.qurlapi.exception.validation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationError {

    private String messageKey;
    private String contextKey;
    private String message;

    public static final class Builder {

        private final ValidationError instance = new ValidationError();

        public ValidationError.Builder withMessageKey(final String messageKey) {
            instance.messageKey = messageKey;
            return this;
        }

        public ValidationError.Builder withContextKey(final String contextKey) {
            instance.contextKey = contextKey;
            return this;
        }

        public ValidationError.Builder withMessage(final String message) {
            instance.message = message;
            return this;
        }

        public ValidationError build() {
            return instance;
        }
    }
}
