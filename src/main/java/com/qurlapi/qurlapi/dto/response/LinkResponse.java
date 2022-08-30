package com.qurlapi.qurlapi.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ApiModel
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class LinkResponse {

    @Getter
    @ApiModelProperty(value = "Shortened link.",
                      name = "rlink",
                      example = "http://localhost:8080/api/urls/dh7f9a73g")
    private String rlink;
}

