package com.qurlapi.qurlapi.service;

import com.qurlapi.qurlapi.assembler.dto.QUrlRequestAssembler;
import com.qurlapi.qurlapi.assembler.model.QUrlAssembler;
import com.qurlapi.qurlapi.dao.QUrlRepository;
import com.qurlapi.qurlapi.dto.request.QUrlRequest;
import com.qurlapi.qurlapi.exception.domain.StampAlreadyExistsException;
import com.qurlapi.qurlapi.exception.domain.StampNotFoundException;
import com.qurlapi.qurlapi.model.QUrl;
import com.qurlapi.qurlapi.util.QUrlIT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@QUrlIT
public class QUrlServiceIT {

    @Autowired
    private QUrlService qUrlService;

    @Autowired
    private QUrlRepository qUrlRepository;

    @BeforeEach
    public void before() {
        qUrlService.purge();
    }

    @Test
    public void shouldFindAllQUrls() {
        // given
        final List<QUrlRequest> qUrls = List.of(QUrlRequestAssembler.any(), QUrlRequestAssembler.any(),
                                                QUrlRequestAssembler.any());
        qUrls.forEach(qUrl -> qUrlService.addQUrl(qUrl));

        // when
        final int actualSize = qUrlService.getAllQUrls()
                                          .size();

        // then
        assertThat(actualSize).isEqualTo(qUrls.size());
    }

    @Test
    public void shouldAddQUrl() {
        // given
        final QUrlRequest expected = QUrlRequestAssembler.any();

        // when
        qUrlService.addQUrl(expected);

        // then
        final Optional<QUrl> optionalQUrl = qUrlRepository.findByStamp(expected.getStamp());
        assertThat(optionalQUrl).isPresent();
        final QUrl actual = optionalQUrl.get();
        assertThat(actual.getUrl()).isEqualTo(expected.getUrl());
        assertThat(actual.getStamp()).isEqualTo(expected.getStamp());
        assertThat(actual.getUsages()).isEqualTo(expected.getUsages());
    }

    @Test
    // TODO: add more asserts
    public void shouldThrowStampAlreadyExistsWhenAddQUrl() {
        // given
        final QUrl qUrl = QUrlAssembler.any();
        qUrlRepository.save(qUrl);

        final QUrlRequest request = QUrlRequestAssembler.make()
                                                        .withStamp(qUrl.getStamp())
                                                        .assemble();

        // when
        final Throwable thrown = catchThrowable(() -> qUrlService.addQUrl(request));

        // then
        assertThat(thrown).isExactlyInstanceOf(StampAlreadyExistsException.class);
    }

    @Test
    public void shouldPurgeAllQUrls() {
        // given
        final List<QUrlRequest> requests = List.of(QUrlRequestAssembler.any(), QUrlRequestAssembler.any(),
                                                   QUrlRequestAssembler.any());
        requests.forEach(request -> qUrlService.addQUrl(request));

        // when
        qUrlService.purge();

        // then
        assertThat(qUrlService.getAllQUrls()
                              .size()).isEqualTo(0);
    }

    @Test
    public void shouldUseLink() {
        // given
        final QUrlRequest request = QUrlRequestAssembler.any();
        qUrlService.addQUrl(request);

        // when
        final QUrl qUrl = qUrlService.useLink(request.getStamp());

        // then
        assertThat(qUrl).isNotNull();
        assertThat(qUrl.getUsages()).isEqualTo(request.getUsages() - 1);
    }

    @Test
    public void shouldUseLinkAndDeleteQUrlWhenExhausted() {
        // given
        final QUrlRequest request = QUrlRequestAssembler.make()
                                                        .withUsages(1)
                                                        .assemble();

        qUrlService.addQUrl(request);

        // when
        final QUrl link = qUrlService.useLink(request.getStamp());

        // then
        final Optional<QUrl> qUrl = qUrlRepository.findByStamp(request.getStamp());
        assertThat(link).isNotNull();
        assertThat(qUrl).isNotPresent();
    }

    @Test
    public void shouldThrowStampNotFoundExceptionWhenGeneratingLink() {
        // given

        // when
        final Throwable thrown = catchThrowable(() -> qUrlService.useLink("not-exists-stamp"));

        // then
        assertThat(thrown).isExactlyInstanceOf(StampNotFoundException.class);
    }
}
