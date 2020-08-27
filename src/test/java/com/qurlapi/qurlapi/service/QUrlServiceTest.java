package com.qurlapi.qurlapi.service;

import com.qurlapi.qurlapi.model.QUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.qurlapi.qurlapi.util.QUrlTestUtils.qUrls;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ActiveProfiles("test")
@SpringBootTest
public class QUrlServiceTest {

    @Autowired
    private QUrlService qUrlService;

    @BeforeEach
    public void before() {
        qUrlService.purge();
    }

    @Test
    public void shouldFindAllQUrls() {
        // given
        final int expectedSize = 3;
        final List<QUrl> qUrls = qUrls();

        // when
        IntStream.range(0, qUrls.size()).forEach(i -> qUrlService.addQUrl(qUrls.get(i)));

        // then
        final int actualSize = qUrlService.getAllQUrls().size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void shouldAddQUrl() {
        // given
        final String stamp = "stamp";
        final String url = "url";
        final int usages = 3;
        final QUrl expected = QUrl.builder()
                .stamp(stamp)
                .url(url)
                .usages(usages)
                .build();

        // when
        qUrlService.addQUrl(expected);

        // then
        final QUrl actual = qUrlService.findQUrlById(expected.getId());
        assertEquals(expected, actual);
    }

    @Test
    public void shouldAddQrlWithUsagesNotGivenAndSetToDefault() {
        // given
        final String stamp = "stamp";
        final String url = "url";
        final QUrl expected = QUrl.builder()
                .stamp(stamp)
                .url(url)
                .build();

        final int expectedUsages = 3;

        // when
        qUrlService.addQUrl(expected);

        // then
        final QUrl actual = qUrlService.findQUrlById(expected.getId());
        final int actualUsages = actual.getUsages();

        assertEquals(expectedUsages, actualUsages);
    }

    @Test
    public void shouldAddQrlWithNegativeUsagesAndSetToDefault() {
        // given
        final String stamp = "stamp";
        final String url = "url";
        final int usages = -1;
        final QUrl expected = QUrl.builder()
                .stamp(stamp)
                .url(url)
                .usages(usages)
                .build();

        final int expectedUsages = 3;

        // when
        qUrlService.addQUrl(expected);

        // then
        final QUrl actual = qUrlService.findQUrlById(expected.getId());
        final int actualUsages = actual.getUsages();

        assertEquals(expectedUsages, actualUsages);
    }

    @Test
    public void shouldAddQrlWithZeroUsagesAndSetToDefault() {
        // given
        final String stamp = "stamp";
        final String url = "url";
        final int usages = 0;
        final QUrl expected = QUrl.builder()
                .stamp(stamp)
                .url(url)
                .usages(usages)
                .build();

        final int expectedUsages = 3;

        // when
        qUrlService.addQUrl(expected);

        // then
        final QUrl actual = qUrlService.findQUrlById(expected.getId());
        final int actualUsages = actual.getUsages();

        assertEquals(expectedUsages, actualUsages);
    }

    @Test
    public void shouldAddQrlWithMoreThanMaxUsagesAndSetToDefault() {
        // given
        final String stamp = "stamp";
        final String url = "url";
        final int usages = Integer.MAX_VALUE;
        final QUrl expected = QUrl.builder()
                .stamp(stamp)
                .url(url)
                .usages(usages)
                .build();

        final int expectedUsages = 3;

        // when
        qUrlService.addQUrl(expected);

        // then
        final QUrl actual = qUrlService.findQUrlById(expected.getId());
        final int actualUsages = actual.getUsages();

        assertEquals(expectedUsages, actualUsages);
    }

    @Test
    public void shouldFindQUrlByStamp() {
        // given
        final String stamp = "stamp";
        final String url = "url";
        final int usages = 3;
        final QUrl expected = QUrl.builder()
                .stamp(stamp)
                .url(url)
                .usages(usages)
                .build();

        qUrlService.addQUrl(expected);

        // when
        final QUrl actual = qUrlService.findQUrlByStamp(stamp);

        // then
        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindQUrlById() {
        // given
        final String stamp = "stamp";
        final String url = "url";
        final int usages = 3;
        final QUrl expected = QUrl.builder()
                .stamp(stamp)
                .url(url)
                .usages(usages)
                .build();

        qUrlService.addQUrl(expected);

        // when
        final QUrl actual = qUrlService.findQUrlById(expected.getId());

        // then
        assertEquals(expected, actual);
    }

    @Test
    public void shouldRemoveQUrl() {
        // given
        final String stamp = "stamp";
        final String url = "url";
        final int usages = 3;
        final QUrl qUrl = QUrl.builder()
                .stamp(stamp)
                .url(url)
                .usages(usages)
                .build();

        qUrlService.addQUrl(qUrl);

        // when
        qUrlService.removeQUrl(qUrl);

        // then
        final QUrl actual = qUrlService.findQUrlById(qUrl.getId());
        assertNull(actual);
    }

    @Test
    public void shouldPurge() {
        // given
        final int expectedSize = 0;
        final List<QUrl> qUrls = qUrls();

        IntStream.range(0, qUrls.size()).forEach(i -> qUrlService.addQUrl(qUrls.get(i)));

        // when
        qUrlService.purge();

        // then
        final int actualSize = qUrlService.getAllQUrls().size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void shouldGenerateLink() {
        // given
        final String stamp = "stamp";
        final String url = "http://example.com";
        final int usages = 3;
        final QUrl qUrl = QUrl.builder()
                .stamp(stamp)
                .url(url)
                .usages(usages)
                .build();

        final String expected = String.format("{\"rlink\":\"%s\"}", qUrl.getUrl());

        qUrlService.addQUrl(qUrl);

        // when
        final String actual = qUrlService.generateLink(qUrl);

        // then
        final int expectedUsages = 2;
        final int actualUsages = qUrlService.findQUrlById(qUrl.getId()).getUsages();

        assertEquals(expected, actual);
        assertEquals(expectedUsages, actualUsages);
    }

    @Test
    public void shouldGenerateLinkWhenZeroUsagesAndDeleteQUrl() {
        // given
        final String stamp = "stamp";
        final String url = "url";
        final int usages = 3;
        final QUrl qUrl = QUrl.builder()
                .stamp(stamp)
                .url(url)
                .usages(usages)
                .build();

        qUrlService.addQUrl(qUrl);

        // when
        final List<String> actualLinks = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            actualLinks.add(qUrlService.generateLink(qUrl));
        }

        // then
        final String expectedBody = String.format("{\"rlink\":\"%s\"}", qUrl.getUrl());
        final QUrl expected = qUrlService.findQUrlById(qUrl.getId());

        actualLinks.forEach(actualLink -> assertEquals(expectedBody, actualLink));
        assertNull(expected);
    }
}
