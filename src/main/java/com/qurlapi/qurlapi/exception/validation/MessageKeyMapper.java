package com.qurlapi.qurlapi.exception.validation;

import java.util.Map;

public final class MessageKeyMapper {

    private static final Map<String, String> CODES = Map.of("NotBlank", MessageKey.NOT_BLANK.getName(),
                                                            "NotNull", MessageKey.NOT_NULL.getName(),
                                                            "Length", MessageKey.LENGTH_VIOLATED.getName(),
                                                            "URL", MessageKey.URL.getName(),
                                                            "Max", MessageKey.MAX.getName());

    public static String map(final String code) {
        return CODES.get(code);
    }
}
