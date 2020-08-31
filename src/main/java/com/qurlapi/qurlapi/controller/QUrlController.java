package com.qurlapi.qurlapi.controller;

import com.qurlapi.qurlapi.dto.request.QUrlRequest;
import com.qurlapi.qurlapi.dto.response.LinkResponse;
import com.qurlapi.qurlapi.dto.response.QUrlResponse;
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
    @ResponseBody
    @GetMapping(path = {"/urls"}, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> urls() {
        return ResponseEntity.ok(qUrlService.getAllQUrlsJson());
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping(path = {"/urls"}, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addUrl(@RequestBody final QUrlRequest request) {
        final ResponseEntity.BodyBuilder badRequest =
                ResponseEntity.status(HttpStatus.BAD_REQUEST);

        final String stamp = request.getStamp();

        if (StringUtils.isEmpty(request.getUrl())) {
            return badRequest.body("{\"message\":\"Url is empty :v\"}");
        }

        if (qUrlService.findQUrlByStamp(stamp) != null) {
            return badRequest.body("{\"message\":\"Stamp taken!\"}");
        }

        LOGGER.info("QUrl request '{}' is being serviced", request);
        qUrlService.addQUrl(request);
        LOGGER.info("QUrl request '{}' has been successfully serviced", request);

        final QUrlResponse response = qUrlService.createQUrlResponse(request);

        return ResponseEntity.ok(response);
    }

    @CrossOrigin
    @GetMapping(path = {"/urls/{stamp}"})
    public ResponseEntity<?> getLink(@PathVariable final String stamp) {
        if (qUrlService.findQUrlByStamp(stamp) == null) {
            LOGGER.info("QUrl with stamp: '{}' not found", stamp);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        final LinkResponse response = qUrlService.generateLink(stamp);
        LOGGER.info("Link has been generated successfully for stamp '{}'", stamp);

        return ResponseEntity.ok(response);
    }

    // TODO: JWT authentication for this endpoint
    @GetMapping(path = {"/purge"})
    public void purge() {
        qUrlService.purge();
    }
}
