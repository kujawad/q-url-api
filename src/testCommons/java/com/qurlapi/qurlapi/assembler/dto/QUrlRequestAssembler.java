package com.qurlapi.qurlapi.assembler.dto;

import com.qurlapi.qurlapi.dto.request.QUrlRequest;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.concurrent.ThreadLocalRandom;

public class QUrlRequestAssembler {
    private String stamp = RandomStringUtils.randomAlphanumeric(10);
    private String url = "https://" + RandomStringUtils.randomAlphanumeric(10) + ".com";
    private int usages = ThreadLocalRandom.current()
                                          .nextInt(3, 10);

    public static QUrlRequestAssembler make() {
        return new QUrlRequestAssembler();
    }

    public static QUrlRequest any() {
        return new QUrlRequestAssembler().assemble();
    }

    public QUrlRequestAssembler withStamp(final String stamp) {
        this.stamp = stamp;
        return this;
    }

    public QUrlRequestAssembler withUrl(final String url) {
        this.url = url;
        return this;
    }

    public QUrlRequestAssembler withUsages(final int usages) {
        this.usages = usages;
        return this;
    }

    public QUrlRequest assemble() {
        return new QUrlRequest(url, stamp, usages);
    }
}
