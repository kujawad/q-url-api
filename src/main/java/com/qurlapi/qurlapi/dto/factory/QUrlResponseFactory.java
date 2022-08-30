package com.qurlapi.qurlapi.dto.factory;

import com.qurlapi.qurlapi.dto.response.QUrlResponse;
import com.qurlapi.qurlapi.model.QUrl;

public class QUrlResponseFactory {

    public static QUrlResponse create(final QUrl qUrl) {
        return QUrlResponse.builder()
                           .url(qUrl.getUrl())
                           .stamp(qUrl.getStamp())
                           .usages(qUrl.getUsages())
                           .build();
    }
}
