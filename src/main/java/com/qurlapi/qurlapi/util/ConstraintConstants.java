package com.qurlapi.qurlapi.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConstraintConstants {

    public static final class QUrl {
        public static final int STAMP_DEFAULT_LENGTH = 7;

        public static final int STAMP_MAX_LENGTH = 64;
        public static final int USAGES_MAX_LENGTH = 128;
        public static final int URL_MAX_LENGTH = 1024;
    }

    public static final class QUrlRequest {
        public static final int USAGES_MIN_LENGTH = 1;
        public static final int URL_MIN_LENGTH = 4;

        public static final int STAMP_MAX_LENGTH = QUrl.STAMP_MAX_LENGTH;
        public static final int USAGES_MAX_LENGTH = QUrl.USAGES_MAX_LENGTH;
        public static final int URL_MAX_LENGTH = QUrl.URL_MAX_LENGTH;
    }
}
