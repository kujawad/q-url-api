package com.qurlapi.qurlapi.config;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.qurlapi.qurlapi.dto.response.ProblemResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.SynthesizedAnnotation;
import org.springframework.nativex.hint.NativeHint;
import org.springframework.nativex.hint.TypeAccess;
import org.springframework.nativex.hint.TypeHint;

@NativeHint(types = {
        @TypeHint(types = PropertyNamingStrategies.KebabCaseStrategy.class, access = TypeAccess.PUBLIC_CONSTRUCTORS),
        @TypeHint(types = {Tag.class, SynthesizedAnnotation.class, ProblemResponse.class})})
@Configuration
public class NativeConfiguration {
}
