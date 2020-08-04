package com.qurlapi.qurlapi.service;

import com.qurlapi.qurlapi.model.QUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

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
        qUrlService.addQUrl(qUrls.get(0));
        qUrlService.addQUrl(qUrls.get(1));
        qUrlService.addQUrl(qUrls.get(2));

        // then
        final int actualSize = qUrlService.getAllQUrls().size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void shouldAddQUrl() {
        // given
        final QUrl expected = QUrl.builder().stamp("stamp").url("url").build();

        // when
        qUrlService.addQUrl(expected);

        // then
        final QUrl actual = qUrlService.findQUrlById(expected.getId());
        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindQUrlByStamp() {
        // given
        final String stamp = "stamp";
        final QUrl expected = QUrl.builder().stamp(stamp).url("url").build();

        // when
        qUrlService.addQUrl(expected);

        // then
        final QUrl actual = qUrlService.findQUrlByStamp(stamp);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindQUrlById() {
        // given
        final QUrl expected = QUrl.builder().stamp("stamp").url("url").build();

        // when
        qUrlService.addQUrl(expected);

        // then
        final QUrl actual = qUrlService.findQUrlById(expected.getId());
        assertEquals(expected, actual);
    }

    @Test
    public void shouldRemoveQUrl() {
        // given
        final QUrl qUrl = QUrl.builder().stamp("stamp").url("url").build();
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

        qUrlService.addQUrl(qUrls.get(0));
        qUrlService.addQUrl(qUrls.get(1));
        qUrlService.addQUrl(qUrls.get(2));

        // when
        qUrlService.purge();

        // then
        final int actualSize = qUrlService.getAllQUrls().size();
        assertEquals(expectedSize, actualSize);
    }
}
