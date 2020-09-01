package com.qurlapi.qurlapi.dto;

import com.qurlapi.qurlapi.validation.StampExists;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class GenericQUrlDTO {

    @NotBlank(message = "Url is empty :v")
    private String url;

    @StampExists(message = "Stamp taken!")
    private String stamp;

    private int usages;
}
