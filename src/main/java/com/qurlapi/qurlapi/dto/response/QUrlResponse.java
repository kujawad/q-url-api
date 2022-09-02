package com.qurlapi.qurlapi.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QUrlResponse {

    @ApiModelProperty(value = "Url to shorten.", name = "url", required = true, example = "https://example.com")
    @NotBlank(message = "Url is empty :v")
    private String url;

    @ApiModelProperty(value = "Stamp for identifying shortened urls.", name = "stamp", example = "something")
    private String stamp;

    @ApiModelProperty(value = "Usages for shortened url.", name = "usages", example = "30")
    @NotNull(message = "{qurl.usages.notnull}")
    @Min(value = 1, message = "{qurl.usages.min}")
    @Max(value = 128, message = "{qurl.usages.max}")
    private Integer usages;

    public static final class Builder {
        private final QUrlResponse instance = new QUrlResponse();

        public QUrlResponse.Builder withUrl(final String url) {
            instance.url = url;
            return this;
        }

        public QUrlResponse.Builder withStamp(final String stamp) {
            instance.stamp = stamp;
            return this;
        }

        public QUrlResponse.Builder withUsages(final Integer usages) {
            instance.usages = usages;
            return this;
        }

        public QUrlResponse build() {
            return instance;
        }
    }
}

