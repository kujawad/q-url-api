package com.qurlapi.qurlapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qurlapi.qurlapi.model.QUrl;
import com.qurlapi.qurlapi.service.QUrlService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = {"/api"})
public class QUrlController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QUrlController.class);
    private final QUrlService qUrlService;

    @Autowired
    public QUrlController(final QUrlService qUrlService) {
        this.qUrlService = qUrlService;
    }

    @CrossOrigin
    @GetMapping(path = {"/urls"}, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> urls() throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        return ResponseEntity.
                ok(mapper.writeValueAsString(qUrlService.getAllQUrls()));
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping(path = {"/urls"}, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addUrl(@RequestBody final QUrl qUrl) {
        final ResponseEntity.BodyBuilder badRequest =
                ResponseEntity.status(HttpStatus.BAD_REQUEST);

        final String stamp = qUrl.getStamp();

        if (StringUtils.isEmpty(qUrl.getUrl())) {
            return badRequest.body("{\"message\":\"Url is empty :v\"}");
        }

        if (qUrlService.findQUrlByStamp(stamp) != null) {
            return badRequest.body("{\"message\":\"Stamp taken!\"}");
        }

        qUrlService.addQUrl(qUrl);
        LOGGER.info("QUrl '{}' has been successfully added", qUrl);

        final String response = qUrlService.createJson(qUrl);

        return ResponseEntity.ok(response);
    }

    @CrossOrigin
    @GetMapping(path = {"/urls/{stamp}"})
    public ResponseEntity<String> link(@PathVariable final String stamp) {
        final QUrl qurl = qUrlService.findQUrlByStamp(stamp);

        if (qurl == null) {
            LOGGER.info("QUrl with stamp: '{}' not found", stamp);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        final String response = qUrlService.generateLink(qurl);
        LOGGER.info("Link has been generated successfully for '{}'", qurl);

        return ResponseEntity.ok(response);
    }

    // TODO: JWT authentication for this endpoint
    @GetMapping(path = {"/purge"})
    public void purge() {
        qUrlService.purge();
    }
}
