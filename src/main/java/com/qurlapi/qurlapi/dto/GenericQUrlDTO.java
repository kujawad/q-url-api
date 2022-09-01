package com.qurlapi.qurlapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.qurlapi.qurlapi.validation.StampExists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel
@Data
@SuperBuilder
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class GenericQUrlDTO {

    @ApiModelProperty(value = "Url to shorten.", name = "url", required = true, example = "https://example.com")
    @NotBlank(message = "Url is empty :v")
    private String url;

    @ApiModelProperty(value = "Stamp for identifying shortened urls.", name = "stamp", example = "something")
    @StampExists(message = "Stamp taken!")
    private String stamp;

    @ApiModelProperty(value = "Usages for shortened url.", name = "usages", example = "30")
    @NotNull(message = "{qurl.usages.notnull}")
    @Min(value = 1, message = "{qurl.usages.min}")
    @Max(value = 128, message = "{qurl.usages.max}")
    private Integer usages;
}