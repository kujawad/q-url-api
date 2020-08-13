package com.qrlapi.qrlapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qrlapi.qrlapi.dao.QrlRepository;
import com.qrlapi.qrlapi.model.Qrl;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
public class QrlService {

    private static final int STAMP_LENGTH = 7;
    private final QrlRepository qrlRepository;

    @Autowired
    public QrlService(final QrlRepository qrlRepository) {
        this.qrlRepository = qrlRepository;
    }

    public List<Qrl> getAllQrls() {
        return qrlRepository.findAll();
    }

    public void addQrl(final Qrl qrl) {
        final String url = qrl.getUrl();

        if (StringUtils.isEmpty(qrl.getStamp())) {
            qrl.setStamp(RandomStringUtils.randomAlphanumeric(STAMP_LENGTH));
        }

        if (!url.startsWith("http")) {
            qrl.setUrl("http://" + url);
        }

        qrlRepository.save(qrl);
    }

    public Qrl findQrlByStamp(final String stamp) {
        return qrlRepository.findByStamp(stamp);
    }

    public Qrl findQrlById(final UUID uuid) {
        return qrlRepository.findById(uuid).orElse(null);
    }

    public void removeQrl(final Qrl qrl) {
        qrlRepository.delete(qrl);
    }

    public void purge() {
        qrlRepository.deleteAll();
    }

    public String createJson(final Qrl qrl) {
        final ObjectMapper mapper = new ObjectMapper();

        String response;

        try {
            response = mapper.writeValueAsString(qrl);
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
