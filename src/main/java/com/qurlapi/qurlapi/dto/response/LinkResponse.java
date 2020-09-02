package com.qurlapi.qurlapi.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@ApiModel
@Data
@Builder
public class LinkResponse {

    @ApiModelProperty(value = "Shortened link.", name = "rlink", example = "http://localhost:8080/api/urls/dh7f9a73g")
    private String rlink;
}
