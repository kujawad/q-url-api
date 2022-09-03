package com.qurlapi.qurlapi.dto.request;

import com.qurlapi.qurlapi.util.ConstraintConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Schema
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QUrlRequest {

    @Schema(description = "Url to shorten.", name = "url", required = true, example = "https://example.com")
    @NotNull(message = "{qurl.url.not-null}")
    @Length(min = ConstraintConstants.QUrlRequest.URL_MIN_LENGTH, max = ConstraintConstants.QUrlRequest.URL_MAX_LENGTH)
    @URL
    private String url;

    @Schema(description = "Stamp for identifying shortened urls.", name = "stamp", example = "something")
    @Length(max = ConstraintConstants.QUrlRequest.STAMP_MAX_LENGTH, message = "{qurl.stamp.max}")
    private String stamp;

    @Schema(description = "Usages for shortened url.", name = "usages", example = "30")
    @NotNull(message = "{qurl.usages.not-null}")
    @Min(value = ConstraintConstants.QUrlRequest.USAGES_MIN_LENGTH, message = "{qurl.usages.min}")
    @Max(value = ConstraintConstants.QUrlRequest.USAGES_MAX_LENGTH, message = "{qurl.usages.max}")
    private int usages;
}
