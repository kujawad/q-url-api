package com.qurlapi.qurlapi.exception.problem;

import java.net.URI;
import java.util.Map;
import java.util.function.Consumer;

public interface Problem {

    URI getType();

    String getTitle();

    Integer getStatus();

    String getMessage();

    Map<String, Object> getExtensions();

    default void ifTypePresent(final Consumer<String> action) {
        final URI type = getType();
        if (type != null) {
            action.accept(type.toString());
        }
    }

}
