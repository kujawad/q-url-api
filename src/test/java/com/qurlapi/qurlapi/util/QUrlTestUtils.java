package com.qurlapi.qurlapi.util;

import com.qurlapi.qurlapi.model.QUrl;

import java.util.Arrays;
import java.util.List;

public class QUrlTestUtils {

    public static final String URL = "/api/urls";

    public static List<QUrl> qUrls() {
        final QUrl qUrl1 = QUrl.builder().stamp("stamp1").url("url1").usages(3).build();
        final QUrl qUrl2 = QUrl.builder().stamp("stamp2").url("url2").usages(3).build();
        final QUrl qUrl3 = QUrl.builder().stamp("stamp3").url("url3").usages(3).build();

        return Arrays.asList(qUrl1, qUrl2, qUrl3);
    }

    public static String expectedGetAllQUrls() {
        return "[{\"url\":\"url1\",\"stamp\":\"stamp1\",\"usages\":3},{\"url\":\"url2\",\"stamp\":\"stamp2\",\"usages\":3},{\"url\":\"url3\",\"stamp\":\"stamp3\",\"usages\":3}]";
    }
}
