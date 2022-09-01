package com.qurlapi.qurlapi.exception.problem;

import java.util.Map;

public interface ValidationError {

    String getContextKey();

    String getMessageKey();

    String getMessage();

    Map<String, Object> getParameters();
}
