package com.qurlapi.qurlapi.controller;

import com.qurlapi.qurlapi.config.QUrlControllerApiInfo;
import com.qurlapi.qurlapi.dto.factory.QUrlResponseFactory;
import com.qurlapi.qurlapi.dto.request.QUrlRequest;
import com.qurlapi.qurlapi.model.QUrl;
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
public class QUrlController implements QUrlControllerApiInfo {
    private final QUrlService qUrlService;

    @Autowired
    public QUrlController(final QUrlService qUrlService) {
        this.qUrlService = qUrlService;
    }

    @GetQUrlsInfo
    @CrossOrigin
    @ResponseBody
    @GetMapping(path = {"/urls"},
                produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> urls() {
        return ResponseEntity.ok(qUrlService.getAllQUrls());
    }

    @AddQUrlInfo
    @CrossOrigin
    @ResponseBody
    @PostMapping(
            path = {"/urls"},
            produces = MimeTypeUtils.APPLICATION_JSON_VALUE,
            consumes = MimeTypeUtils.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> addUrl(@Valid @RequestBody final QUrlRequest request) {
        final QUrl qUrl = qUrlService.addQUrl(request);
        return ResponseEntity.ok(QUrlResponseFactory.create(qUrl));
    }

    @GetLinkInfo
    @CrossOrigin
    @ResponseBody
    @GetMapping(path = {"/urls/{stamp}"},
                produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLink(@StampExists(isPathVariable = true) @PathVariable final String stamp) {
        return ResponseEntity.ok(qUrlService.generateLink(stamp));
    }

    // TODO: JWT authentication for this endpoint
    @PurgeInfo
    @GetMapping(path = {"/purge"})
    public void purge() {
        qUrlService.purge();
    }
}