package com.qurlapi.qurlapi.util;

import com.qurlapi.qurlapi.dto.request.QUrlRequest;
import com.qurlapi.qurlapi.model.QUrl;

import java.util.List;

public class QUrlTestUtils {

    public static final String URL = "/api/urls";

    public static List<QUrl> qUrls() {
        final QUrl qUrl1 = new QUrl.Builder().withStamp("stamp1")
                                             .withUrl("url1")
                                             .withUsages(3)
                                             .build();
        final QUrl qUrl2 = new QUrl.Builder().withStamp("stamp2")
                                             .withUrl("url2")
                                             .withUsages(3)
                                             .build();
        final QUrl qUrl3 = new QUrl.Builder().withStamp("stamp3")
                                             .withUrl("url3")
                                             .withUsages(3)
                                             .build();

        return List.of(qUrl1, qUrl2, qUrl3);
    }

    public static List<QUrlRequest> qUrlResponses() {
        final QUrlRequest qUrl1 = QUrlRequest.builder()
                                             .stamp("stamp1")
                                             .url("url1")
                                             .usages(3)
                                             .build();
        final QUrlRequest qUrl2 = QUrlRequest.builder()
                                             .stamp("stamp2")
                                             .url("url2")
                                             .usages(3)
                                             .build();
        final QUrlRequest qUrl3 = QUrlRequest.builder()
                                             .stamp("stamp3")
                                             .url("url3")
                                             .usages(3)
                                             .build();

        return List.of(qUrl1, qUrl2, qUrl3);
    }
}
