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
        public String getMessage() {
            return "QUrl stamp with identifier {0} not found";
        }

        @Override
        public HttpStatus getHttpStatus() {
            return HttpStatus.NOT_FOUND;
        }
    };

    public static final ApplicationProblem STAMP_ALREADY_EXISTS = new ApplicationProblem() {
        @Override
        public URI getType() {
            return URI.create("stamp-already-exists");
        }

        @Override
        public String getTitle() {
            return "Stamp already exists";
        }

        @Override
        public String getMessage() {
            return "QUrl with stamp identifier {0} already exists";
        }

        @Override
        public HttpStatus getHttpStatus() {
            return HttpStatus.CONFLICT;
        }
    };
}
