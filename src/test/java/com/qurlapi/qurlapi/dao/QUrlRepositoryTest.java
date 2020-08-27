package com.qurlapi.qurlapi.dao;

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
public class QUrlRepositoryTest {

    @Autowired
    private QUrlRepository qUrlRepository;

    @BeforeEach
    public void before() {
        qUrlRepository.deleteAll();
    }

    @Test
    public void shouldSaveQUrl() {
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
        qUrlRepository.save(expected);

        // then
        final QUrl actual = qUrlRepository.findById(expected.getId()).orElse(null);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldSaveAll() {
        // given
        final int expectedSize = 3;
        final List<QUrl> qUrls = qUrls();

        // when
        qUrlRepository.saveAll(qUrls);

        // then
        final int actualSize = qUrlRepository.findAll().size();
        assertEquals(expectedSize, actualSize);
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

        // when
        qUrlRepository.save(expected);

        // then
        final QUrl actual = qUrlRepository.findByStamp(stamp);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldDeleteOneQUrl() {
        // given
        final String stamp = "stamp";
        final String url = "url";
        final int usages = 3;
        final QUrl qUrl = QUrl.builder()
                .stamp(stamp)
                .url(url)
                .usages(usages)
                .build();

        qUrlRepository.save(qUrl);

        // when
        qUrlRepository.delete(qUrl);

        // then
        final QUrl actual = qUrlRepository.findById(qUrl.getId()).orElse(null);
        assertNull(actual);
    }

    @Test
    public void shouldDeleteAll() {
        // given
        final int expectedSize = 0;
        final List<QUrl> qUrls = qUrls();

        qUrlRepository.saveAll(qUrls);

        // when
        qUrlRepository.deleteAll();

        // then
        final int actualSize = qUrlRepository.findAll().size();
        assertEquals(expectedSize, actualSize);
    }
}
