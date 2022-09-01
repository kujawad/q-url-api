package com.qurlapi.qurlapi.exception.domain.problem;

import com.qurlapi.qurlapi.exception.problem.ApplicationProblem;
import org.springframework.http.HttpStatus;

import java.net.URI;

public abstract class QUrlProblem {

    public static final ApplicationProblem STAMP_NOT_FOUND = new ApplicationProblem() {
        @Override
        public URI getType() {
            return URI.create("stamp-not-found");
        }

        @Override
        public String getTitle() {
            return "Stamp not found";
        }

        @Override
        public String getDetail() {
            return "Stamp with identifier {0} not found";
        }

        @Override
        public HttpStatus getHttpStatus() {
            return HttpStatus.NOT_FOUND;
        }
    };
}
