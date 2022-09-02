package com.qurlapi.qurlapi.dto.factory;

import com.qurlapi.qurlapi.dto.response.QUrlResponse;
import com.qurlapi.qurlapi.model.QUrl;

import java.util.List;
import java.util.stream.Collectors;

public class QUrlResponseFactory {

    public static QUrlResponse create(final QUrl qUrl) {
        return new QUrlResponse.Builder().withUrl(qUrl.getUrl())
                                         .withStamp(qUrl.getStamp())
                                         .withUsages(qUrl.getUsages())
                                         .build();
    }

    public static List<QUrlResponse> create(final List<QUrl> qUrls) {
        return qUrls.stream()
                    .map(QUrlResponseFactory::create)
                    .collect(Collectors.toList());
    }
}
