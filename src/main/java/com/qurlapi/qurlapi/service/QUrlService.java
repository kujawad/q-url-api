package com.qurlapi.qurlapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qurlapi.qurlapi.dao.QUrlRepository;
import com.qurlapi.qurlapi.dto.request.QUrlRequest;
import com.qurlapi.qurlapi.dto.response.LinkResponse;
import com.qurlapi.qurlapi.dto.response.QUrlResponse;
import com.qurlapi.qurlapi.model.QUrl;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QUrlService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QUrlService.class);

    private static final int STAMP_LENGTH = 7;

    private static final int MIN_USAGES = 0;
    private static final int DEFAULT_USAGES = 3;
    private static final int MAX_USAGES = 100;

    private final QUrlRepository qUrlRepository;

    @Autowired
    public QUrlService(final QUrlRepository qUrlRepository) {
        this.qUrlRepository = qUrlRepository;
    }

    public List<QUrlResponse> getAllQUrls() {
        final List<QUrl> qUrls = qUrlRepository.findAll();
        return qUrls.stream()
                .map(q -> QUrlResponse.builder()
                        .url(q.getUrl())
                        .stamp(q.getStamp())
                        .usages(q.getUsages())
                        .build())
                .collect(Collectors.toList());
    }

    public String getAllQUrlsJson() {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(getAllQUrls());
        } catch (final JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void addQUrl(final QUrlRequest request) {
        final String url = request.getUrl();
        final String stamp = request.getStamp();
        final int usages = request.getUsages();

        final QUrl.QUrlBuilder builder = QUrl.builder()
                .url(url)
                .stamp(stamp)
                .usages(usages);

        if (StringUtils.isEmpty(stamp)) {
            builder.stamp(RandomStringUtils.randomAlphanumeric(STAMP_LENGTH));
        }

        if (!url.startsWith("http")) {
            builder.url("http://" + url);
        }

        if (usages <= MIN_USAGES || usages > MAX_USAGES) {
            builder.usages(DEFAULT_USAGES);
        }

        final QUrl qUrl = builder.build();

        request.setStamp(qUrl.getStamp());
        request.setUsages(qUrl.getUsages());

        qUrlRepository.save(qUrl);
    }

    public QUrlResponse findQUrlByStamp(final String stamp) {
        final QUrl qUrl = qUrlRepository.findByStamp(stamp).orElse(null);
        return qUrl == null ? null : QUrlResponse.builder()
                .url(qUrl.getUrl())
                .stamp(qUrl.getStamp())
                .usages(qUrl.getUsages())
                .build();
    }

    public void removeQUrl(final QUrlRequest request) {
        final Optional<QUrl> optionalQUrl = qUrlRepository.findByStamp(request.getStamp());

        optionalQUrl.ifPresent(qUrlRepository::delete);
    }

    public void purge() {
        qUrlRepository.deleteAll();
    }

    public QUrlResponse createQUrlResponse(final QUrlRequest request) {
        return QUrlResponse.builder()
                .url(request.getUrl())
                .stamp(request.getStamp())
                .usages(request.getUsages())
                .build();
    }

    public LinkResponse generateLink(final String stamp) {
        final Optional<QUrl> optionalQUrl = qUrlRepository.findByStamp(stamp);

        if (optionalQUrl.isEmpty()) {
            return null;
        }

        final QUrl qUrl = optionalQUrl.get();

        final LinkResponse response = LinkResponse.builder()
                .rlink(qUrl.getUrl())
                .build();

        qUrl.setUsages(qUrl.getUsages() - 1);
        qUrlRepository.save(qUrl);

        if (qUrl.getUsages() == 0) {
            LOGGER.info("'{}' reached 0 usages. Removing from database.", qUrl);
            qUrlRepository.delete(qUrl);
        }

        return response;
    }
}
