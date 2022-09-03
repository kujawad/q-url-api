package com.qurlapi.qurlapi.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.qurlapi.qurlapi.exception.problem.ApplicationProblem;
import com.qurlapi.qurlapi.exception.validation.ValidationError;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Schema
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class ProblemResponse implements ApplicationProblem {

    @Schema(description = "Exception type.", name = "type", example = "stamp-not-exists")
    private URI type;

    @Schema(description = "Status of exception response.", name = "status", example = "400")
    private Integer status;

    @Schema(description = "Exception title.", name = "title", example = "Stamp does not exists!")
    private String title;

    @Schema(description = "Exception details.", name = "message",
                      example = "Stamp with identifier d8f6a3SDF does not exists!")
    private String message;

    @Schema(description = "Problem extensions", name = "extensions", example = "{}")
    private Map<String, Object> extensions;

    @Schema(description = "List of validation errors.", name = "validationErrors", example = "[]")
    private List<ValidationError> validationErrors;

    public ProblemResponse(final HttpStatus httpStatus) {
        this.status = httpStatus.value();
    }

    @Override
    @JsonIgnore
    public HttpStatus getHttpStatus() {
        return HttpStatus.resolve(this.status);
    }

    public static class Builder {

        private final ProblemResponse instance = new ProblemResponse();

        public ProblemResponse.Builder withType(final String type) {
            instance.type = URI.create(type);
            return this;
        }

        public ProblemResponse.Builder withTitle(final String title) {
            instance.title = title;
            return this;
        }

        public ProblemResponse.Builder withDetail(final String detail) {
            instance.message = detail;
            return this;
        }

        public ProblemResponse.Builder withStatus(final int status) {
            instance.status = status;
            return this;
        }

        public ProblemResponse.Builder withValidationErrors(final List<ValidationError> validationErrors) {
            instance.validationErrors = validationErrors;
            return this;
        }

        public ProblemResponse build() {
            return instance;
        }
    }
}
