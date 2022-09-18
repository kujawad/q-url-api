package com.qurlapi.qurlapi.config;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.qurlapi.qurlapi.dto.response.ProblemResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.SynthesizedAnnotation;
import org.springframework.nativex.hint.JdkProxyHint;
import org.springframework.nativex.hint.NativeHint;
import org.springframework.nativex.hint.TypeAccess;
import org.springframework.nativex.hint.TypeHint;

@NativeHint(jdkProxies = {@JdkProxyHint(types = {Tag.class, SynthesizedAnnotation.class})},
            types = {@TypeHint(types = {ProblemResponse.class}),
                     @TypeHint(types = PropertyNamingStrategies.KebabCaseStrategy.class,
                               access = TypeAccess.PUBLIC_CONSTRUCTORS)})
@Configuration
public class NativeConfiguration {
}
