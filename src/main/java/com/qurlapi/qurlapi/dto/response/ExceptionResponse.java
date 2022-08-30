package com.qurlapi.qurlapi.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {

    @ApiModelProperty(value = "Status of exception response.",
                      name = "status",
                      example = "400")
    private int status;

    @ApiModelProperty(
            value = "A String containing the name or path of the servlet being called, as specified in the request URL.",
            name = "path",
            example = "/api/urls"
    )
    private String path;

    @ApiModelProperty(value = "Exception message.",
                      name = "message",
                      example = "Stamp does not exists!")
    private String message;

    @ApiModelProperty(value = "Timestamp of exception.",
                      name = "timestamp",
                      example = "1598990725")
    private long timestamp;
}
