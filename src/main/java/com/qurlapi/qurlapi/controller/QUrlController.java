package com.qurlapi.qurlapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qurlapi.qurlapi.model.QUrl;
import com.qurlapi.qurlapi.service.QUrlService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = {"/api"})
public class QUrlController {

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
        final String response = qUrlService.createJson(qUrl);

        return ResponseEntity.ok(response);
    }

    @CrossOrigin
    @GetMapping(path = {"/urls/{stamp}"})
    public ResponseEntity<?> redirect(@PathVariable final String stamp) {
        final QUrl qurl = qUrlService.findQUrlByStamp(stamp);
        final ResponseEntity.BodyBuilder response =
                ResponseEntity.status(HttpStatus.FOUND);

        if (qurl == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        final String url = qurl.getUrl();
        qUrlService.removeQUrl(qurl);

        if (!url.startsWith("http")) {
            return response.header(HttpHeaders.LOCATION, "http://" + url).build();
        }

        return response.header(HttpHeaders.LOCATION, url).build();
    }

    @GetMapping(path = {"/purge"})
    public void purge() {
        qUrlService.purge();
    }
}
