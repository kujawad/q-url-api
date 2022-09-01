package com.qurlapi.qurlapi.exception.problem;

import java.net.URI;

public class ProblemTypeResolver {
    public static URI resolve(final String s, final ApplicationProblem problem) {
        return URI.create(s);
    }
}
