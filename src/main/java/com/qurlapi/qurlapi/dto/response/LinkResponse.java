package com.qurlapi.qurlapi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class LinkResponse {

    @Getter
    @Schema(description = "Shortened link.", name = "rlink", example = "http://localhost:8080/api/urls/dh7f9a73g")
    private String rlink;
}

