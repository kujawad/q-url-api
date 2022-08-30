package com.qurlapi.qurlapi.service;

import com.qurlapi.qurlapi.dao.QUrlRepository;
import com.qurlapi.qurlapi.dto.request.QUrlRequest;
import com.qurlapi.qurlapi.dto.response.LinkResponse;
import com.qurlapi.qurlapi.dto.response.QUrlResponse;
import com.qurlapi.qurlapi.model.QUrl;
import com.qurlapi.qurlapi.util.ConstraintConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
//TODO: make sure all methods return model
public class QUrlService {


    private final QUrlRepository qUrlRepository;

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

    @Transactional
    public QUrl addQUrl(final QUrlRequest request) {
        final String url = request.getUrl();
        final String stamp = request.getStamp();
        final Integer usages = request.getUsages();

        final QUrl.Builder builder = new QUrl.Builder().withUrl(url)
                                                       .withStamp(stamp)
                                                       .withUsages(usages);

        if (!StringUtils.hasText(stamp)) {
            builder.withStamp(RandomStringUtils.randomAlphanumeric(ConstraintConstants.QUrl.STAMP_DEFAULT_LENGTH));
        }

        if (!url.startsWith("http")) {
            log.info("QUrl request URL didn't start with 'http', setting to default.");
            builder.withUrl("https://" + url);
        }

        if (usages <= ConstraintConstants.QUrl.USAGES_MIN_LENGTH || usages > ConstraintConstants.QUrl.USAGES_MAX_LENGTH) {
            log.info("QUrl request usages violated default restrictions, setting to default.");
            builder.withUsages(ConstraintConstants.QUrl.USAGES_DEFAULT_LENGTH);
        }

        final QUrl qUrl = builder.build();
        return qUrlRepository.save(qUrl);
    }

    public void purge() {
        qUrlRepository.deleteAll();
    }

    @Transactional
    public LinkResponse generateLink(final String stamp) {
        final QUrl qUrl = qUrlRepository.findByStamp(stamp)
                                        .orElseThrow();
        qUrl.use();

        if (qUrl.getUsages() == 0) {
            log.info("'{}' reached 0 usages. Removing from database.", qUrl);
            qUrlRepository.delete(qUrl);
        } else {
            qUrlRepository.save(qUrl);
        }

        return LinkResponse.builder()
                           .rlink(qUrl.getUrl())
                           .build();
    }
}
