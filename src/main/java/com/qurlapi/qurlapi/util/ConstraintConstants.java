package com.qurlapi.qurlapi.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConstraintConstants {

    public static final class QUrl {
        public static final int STAMP_DEFAULT_LENGTH = 7;
        public static final int USAGES_DEFAULT_LENGTH = 3;

        public static final int USAGES_MIN_LENGTH = 0;

        public static final int STAMP_MAX_LENGTH = 128;
        public static final int USAGES_MAX_LENGTH = 128;
        public static final int URL_MAX_LENGTH = 256;
    }
}
