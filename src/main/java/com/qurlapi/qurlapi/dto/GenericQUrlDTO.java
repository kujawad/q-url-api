package com.qurlapi.qurlapi.dto;

import com.qurlapi.qurlapi.validation.StampExists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@ApiModel
@Data
@SuperBuilder
@NoArgsConstructor
public abstract class GenericQUrlDTO {

    @ApiModelProperty(value = "Url to shorten.", name = "url", required = true, example = "https://example.com")
    @NotBlank(message = "Url is empty :v")
    private String url;

    @ApiModelProperty(value = "Stamp for identifying shortened urls.", name = "stamp", example = "something")
    @StampExists(message = "Stamp taken!")
    private String stamp;

    @ApiModelProperty(value = "Usages for shortened url.", name = "usages", example = "30")
    private int usages;
}