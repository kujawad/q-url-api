package com.qurlapi.qurlapi.service;

import com.qurlapi.qurlapi.dao.QUrlRepository;
import com.qurlapi.qurlapi.model.QUrl;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
public class QUrlService {

    private static final int STAMP_LENGTH = 7;
    private final QUrlRepository qUrlRepository;

    @Autowired
    public QUrlService(final QUrlRepository qUrlRepository) {
        this.qUrlRepository = qUrlRepository;
    }

    public List<QUrl> getAllQUrls() {
        return qUrlRepository.findAll();
    }

    public void addQUrl(final QUrl qUrl) {
        if (StringUtils.isEmpty(qUrl.getStamp())) {
            qUrl.setStamp(RandomStringUtils.randomAlphanumeric(STAMP_LENGTH));
        }

        qUrlRepository.save(qUrl);
    }

    public QUrl findQUrlByStamp(final String stamp) {
        return qUrlRepository.findByStamp(stamp);
    }

    public QUrl findQUrlById(final UUID uuid) {
        return qUrlRepository.findById(uuid).orElse(null);
    }

    public void removeQUrl(final QUrl qUrl) {
        qUrlRepository.delete(qUrl);
    }

    public void purge() {
        qUrlRepository.deleteAll();
    }
}
