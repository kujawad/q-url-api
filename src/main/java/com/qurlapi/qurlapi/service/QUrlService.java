package com.qurlapi.qurlapi.service;

import com.qurlapi.qurlapi.dao.QUrlRepository;
import com.qurlapi.qurlapi.dto.request.QUrlRequest;
import com.qurlapi.qurlapi.exception.domain.StampNotFoundException;
import com.qurlapi.qurlapi.exception.domain.problem.QUrlProblem;
import com.qurlapi.qurlapi.model.QUrl;
import com.qurlapi.qurlapi.util.ConstraintConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
public class QUrlService {

    private final QUrlRepository qUrlRepository;

    public QUrlService(final QUrlRepository qUrlRepository) {
        this.qUrlRepository = qUrlRepository;
    }

    public List<QUrl> getAllQUrls() {
        return qUrlRepository.findAll();
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
            log.info("QUrl '{}' request URL didn't start with 'http', setting to default.", request);
            builder.withUrl("https://" + url);
        }

        if (usages <= ConstraintConstants.QUrl.USAGES_MIN_LENGTH ||
            usages > ConstraintConstants.QUrl.USAGES_MAX_LENGTH) {
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
    public QUrl useLink(final String stamp) {
        int a = 3;
        final QUrl qUrl = qUrlRepository.findByStamp(stamp)
                                        .orElseThrow(
                                                () -> new StampNotFoundException(QUrlProblem.STAMP_NOT_FOUND, stamp));
        qUrl.use();

        if (qUrl.getUsages() == 0) {
            qUrlRepository.delete(qUrl);
            return qUrl;
        } else {
            return qUrlRepository.save(qUrl);
        }
    }
}
