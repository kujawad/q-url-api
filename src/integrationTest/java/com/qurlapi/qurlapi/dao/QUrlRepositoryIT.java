package com.qurlapi.qurlapi.dao;

import com.qurlapi.qurlapi.assembler.model.QUrlAssembler;
import com.qurlapi.qurlapi.model.QUrl;
import com.qurlapi.qurlapi.util.QUrlIT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QUrlIT
public class QUrlRepositoryIT {

    @Autowired
    private QUrlRepository qUrlRepository;

    @BeforeEach
    void before() {
        qUrlRepository.deleteAll();
    }

    @Test
    public void shouldSaveQUrl() {
        // given
        final QUrl qUrl = QUrlAssembler.any();

        // when
        qUrlRepository.save(qUrl);

        // then
        final int expectedSize = 1;
        final int actualSize = qUrlRepository.findAll()
                                             .size();

        assertThat(actualSize).isEqualTo(expectedSize);
    }

    @Test
    public void shouldSaveAll() {
        // given
        final List<QUrl> qUrls = List.of(QUrlAssembler.any(), QUrlAssembler.any(), QUrlAssembler.any());

        // when
        qUrlRepository.saveAll(qUrls);

        // then
        final int actualSize = qUrlRepository.findAll()
                                             .size();
        assertThat(actualSize).isEqualTo(qUrls.size());
    }

    @Test
    public void shouldFindQUrlByStamp() {
        // given
        final QUrl expected = QUrlAssembler.any();
        qUrlRepository.save(expected);

        // when
        final QUrl actual = qUrlRepository.findByStamp(expected.getStamp())
                                          .orElse(null);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldDeleteOneQUrl() {
        // given
        final QUrl qUrl = QUrlAssembler.any();
        qUrlRepository.save(qUrl);

        // when
        qUrlRepository.delete(qUrl);

        // then
        final QUrl actual = qUrlRepository.findById(qUrl.getId())
                                          .orElse(null);
        assertThat(actual).isNull();
    }

    @Test
    public void shouldDeleteAll() {
        // given
        final int expectedSize = 0;
        final List<QUrl> qUrls = List.of(QUrlAssembler.any(), QUrlAssembler.any(), QUrlAssembler.any());

        qUrlRepository.saveAll(qUrls);

        // when
        qUrlRepository.deleteAll();

        // then
        final int actualSize = qUrlRepository.findAll()
                                             .size();
        assertThat(actualSize).isEqualTo(expectedSize);
    }
}
