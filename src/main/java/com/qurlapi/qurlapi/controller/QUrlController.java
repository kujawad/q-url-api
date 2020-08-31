package com.qurlapi.qurlapi.controller;

import com.qurlapi.qurlapi.dto.request.QUrlRequest;
import com.qurlapi.qurlapi.service.QUrlService;
import com.qurlapi.qurlapi.validation.StampExists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping(path = {"/api"})
public class QUrlController {
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
    public ResponseEntity<?> addUrl(@Valid @RequestBody final QUrlRequest request) {
        qUrlService.addQUrl(request);
        return ResponseEntity.ok(qUrlService.createQUrlResponse(request));
    }

    @CrossOrigin
    @GetMapping(path = {"/urls/{stamp}"})
    public ResponseEntity<?> getLink(@StampExists(isPathVariable = true) @PathVariable final String stamp) {
        return ResponseEntity.ok(qUrlService.generateLink(stamp));
    }

    // TODO: JWT authentication for this endpoint
    @GetMapping(path = {"/purge"})
    public void purge() {
        qUrlService.purge();
    }
}
