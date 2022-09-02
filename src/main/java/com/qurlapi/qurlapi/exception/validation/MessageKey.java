package com.qurlapi.qurlapi.exception.validation;

public enum MessageKey {

    URL,

    MAX,

    NOT_NULL,

    NOT_BLANK,

    LENGTH_VIOLATED;

    public String getName() {
        final String dashedName = name().replaceAll("_", "-");
        return dashedName.toLowerCase();
    }
}
