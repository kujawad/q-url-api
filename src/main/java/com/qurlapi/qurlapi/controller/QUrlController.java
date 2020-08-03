package com.qurlapi.qurlapi.controller;

import com.qurlapi.qurlapi.model.Url;
import com.qurlapi.qurlapi.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = {"/api"})
public class QUrlController {

    private final UrlService urlService;

    @Autowired
    public QUrlController(final UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping(path = {"/urls"})
    public List<Url> urls() {
        return urlService.getAllUrls();
    }

    @PostMapping(path = {"/urls"})
    public ResponseEntity<?> addUrl(@RequestBody final Url url) {
        if (url.getUrl() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        urlService.addUrl(url);
        return ResponseEntity.ok(url);
    }
}
