package com.qurlapi.qurlapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class GenericQUrlDto {

    private String url;

    private String stamp;

    private int usages;
}
