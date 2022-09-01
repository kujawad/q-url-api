package com.qurlapi.qurlapi.exception.code;

public enum MessageKey {

    NOT_NULL;

    public String getName() {
        final String dashedName = name().replaceAll("_", "-");
        return dashedName.toLowerCase();
    }
}
