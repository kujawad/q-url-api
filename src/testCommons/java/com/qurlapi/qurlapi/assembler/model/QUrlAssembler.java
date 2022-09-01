package com.qurlapi.qurlapi.assembler.model;

import com.qurlapi.qurlapi.model.QUrl;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.concurrent.ThreadLocalRandom;

public class QUrlAssembler {
    private String stamp = RandomStringUtils.randomAlphanumeric(10);
    private String url = "https://" + RandomStringUtils.randomAlphanumeric(10) + ".com";
    private int usages = ThreadLocalRandom.current()
                                          .nextInt(3, 10);

    public static QUrlAssembler make() {
        return new QUrlAssembler();
    }

    public static QUrl any() {
        return new QUrlAssembler().assemble();
    }

    public QUrlAssembler withStamp(final String stamp) {
        this.stamp = stamp;
        return this;
    }

    public QUrlAssembler withUrl(final String url) {
        this.url = url;
        return this;
    }

    public QUrlAssembler withUsages(final int usages) {
        this.usages = usages;
        return this;
    }

    public QUrl assemble() {
        return new QUrl.Builder().withUrl(url)
                                 .withStamp(stamp)
                                 .withUsages(usages)
                                 .build();
    }
}
