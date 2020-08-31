package com.qurlapi.qurlapi.service;

import com.qurlapi.qurlapi.dto.request.QUrlRequest;
import com.qurlapi.qurlapi.dto.response.LinkResponse;
import com.qurlapi.qurlapi.dto.response.QUrlResponse;
import com.qurlapi.qurlapi.util.QUrlTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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
        final List<QUrlRequest> qUrls = QUrlTestUtils.qUrlRequests();

        IntStream.range(0, qUrls.size()).forEach(i -> qUrlService.addQUrl(qUrls.get(i)));

        // when
        final int actualSize = qUrlService.getAllQUrls().size();

        // then
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void shouldAddQUrl() {
        // given
        final String stamp = "stamp";
        final String url = "http://example.com";
        final int usages = 3;
        final QUrlRequest expected = QUrlRequest.builder()
                .stamp(stamp)
                .url(url)
                .usages(usages)
                .build();

        // when
        qUrlService.addQUrl(expected);

        // then
        final QUrlResponse actual = qUrlService.findQUrlByStamp(expected.getStamp());
        assertEquals(expected.getUrl(), actual.getUrl());
        assertEquals(expected.getStamp(), actual.getStamp());
        assertEquals(expected.getUsages(), actual.getUsages());
    }

    @Test
    public void shouldAddQrlWithUsagesNotGivenAndSetToDefault() {
        // given
        final String stamp = "stamp";
        final String url = "url";
        final QUrlRequest expected = QUrlRequest.builder()
                .stamp(stamp)
                .url(url)
                .build();

        final int expectedUsages = 3;

        // when
        qUrlService.addQUrl(expected);

        // then
        final QUrlResponse actual = qUrlService.findQUrlByStamp(expected.getStamp());
        final int actualUsages = actual.getUsages();

        assertEquals(expectedUsages, actualUsages);
    }

    @Test
    public void shouldAddQrlWithNegativeUsagesAndSetToDefault() {
        // given
        final String stamp = "stamp";
        final String url = "url";
        final int usages = -1;
        final QUrlRequest expected = QUrlRequest.builder()
                .stamp(stamp)
                .url(url)
                .usages(usages)
                .build();

        final int expectedUsages = 3;

        // when
        qUrlService.addQUrl(expected);

        // then
        final QUrlResponse actual = qUrlService.findQUrlByStamp(expected.getStamp());
        final int actualUsages = actual.getUsages();

        assertEquals(expectedUsages, actualUsages);
    }

    @Test
    public void shouldAddQrlWithZeroUsagesAndSetToDefault() {
        // given
        final String stamp = "stamp";
        final String url = "url";
        final int usages = 0;
        final QUrlRequest expected = QUrlRequest.builder()
                .stamp(stamp)
                .url(url)
                .usages(usages)
                .build();

        final int expectedUsages = 3;

        // when
        qUrlService.addQUrl(expected);

        // then
        final QUrlResponse actual = qUrlService.findQUrlByStamp(expected.getStamp());
        final int actualUsages = actual.getUsages();

        assertEquals(expectedUsages, actualUsages);
    }

    @Test
    public void shouldAddQrlWithMoreThanMaxUsagesAndSetToDefault() {
        // given
        final String stamp = "stamp";
        final String url = "url";
        final int usages = Integer.MAX_VALUE;
        final QUrlRequest expected = QUrlRequest.builder()
                .stamp(stamp)
                .url(url)
                .usages(usages)
                .build();

        final int expectedUsages = 3;

        // when
        qUrlService.addQUrl(expected);

        // then
        final QUrlResponse actual = qUrlService.findQUrlByStamp(expected.getStamp());
        final int actualUsages = actual.getUsages();

        assertEquals(expectedUsages, actualUsages);
    }

    @Test
    public void shouldFindQUrlByStamp() {
        // given
        final String stamp = "stamp";
        final String url = "http://example.com";
        final int usages = 3;
        final QUrlRequest expected = QUrlRequest.builder()
                .stamp(stamp)
                .url(url)
                .usages(usages)
                .build();

        qUrlService.addQUrl(expected);

        // when
        final QUrlResponse actual = qUrlService.findQUrlByStamp(stamp);

        // then
        assertEquals(expected.getUrl(), actual.getUrl());
        assertEquals(expected.getStamp(), actual.getStamp());
        assertEquals(expected.getUsages(), actual.getUsages());
    }

    @Test
    public void shouldRemoveQUrl() {
        // given
        final String stamp = "stamp";
        final String url = "url";
        final int usages = 3;
        final QUrlRequest request = QUrlRequest.builder()
                .stamp(stamp)
                .url(url)
                .usages(usages)
                .build();

        qUrlService.addQUrl(request);

        // when
        qUrlService.removeQUrl(request);

        // then
        final QUrlResponse actual = qUrlService.findQUrlByStamp(request.getStamp());
        assertNull(actual);
    }

    @Test
    public void shouldPurge() {
        // given
        final int expectedSize = 0;
        final List<QUrlRequest> requests = QUrlTestUtils.qUrlRequests();

        IntStream.range(0, requests.size()).forEach(i -> qUrlService.addQUrl(requests.get(i)));

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
        final QUrlRequest request = QUrlRequest.builder()
                .stamp(stamp)
                .url(url)
                .usages(usages)
                .build();

        final LinkResponse expected = LinkResponse.builder()
                .rlink(request.getUrl())
                .build();

        qUrlService.addQUrl(request);

        // when
        final LinkResponse actual = qUrlService.generateLink(stamp);

        // then
        final int expectedUsages = 2;
        final int actualUsages = qUrlService.findQUrlByStamp(request.getStamp()).getUsages();

        assertEquals(expected, actual);
        assertEquals(expectedUsages, actualUsages);
    }

    @Test
    public void shouldGenerateLinkWhenZeroUsagesAndDeleteQUrl() {
        // given
        final String stamp = "stamp";
        final String url = "http://example.com";
        final int usages = 3;
        final QUrlRequest request = QUrlRequest.builder()
                .stamp(stamp)
                .url(url)
                .usages(usages)
                .build();

        qUrlService.addQUrl(request);

        // when
        final List<LinkResponse> actualLinks = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            actualLinks.add(qUrlService.generateLink(stamp));
        }

        // then
        final QUrlResponse expected = qUrlService.findQUrlByStamp(stamp);

        actualLinks.forEach(actualLink -> assertEquals(url, actualLink.getRlink()));
        assertNull(expected);
    }
}
