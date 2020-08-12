package com.qurlapi.qurlapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qurlapi.qurlapi.dao.QUrlRepository;
import com.qurlapi.qurlapi.model.QUrl;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
        final String url = qUrl.getUrl();

        if (StringUtils.isEmpty(qUrl.getStamp())) {
            qUrl.setStamp(RandomStringUtils.randomAlphanumeric(STAMP_LENGTH));
        }

        if (!url.startsWith("http")) {
            qUrl.setUrl("http://" + url);
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

    public String createJson(final QUrl qUrl) {
        final ObjectMapper mapper = new ObjectMapper();

        String response;

        try {
            response = mapper.writeValueAsString(qUrl);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public String generateLink(final String url) {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode rootNode = mapper.createObjectNode();

        rootNode.put("rlink", url);

        String link;

        try {
            link = mapper.writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return link;
    }
}
