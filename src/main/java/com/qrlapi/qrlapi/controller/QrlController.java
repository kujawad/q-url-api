package com.qrlapi.qrlapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qrlapi.qrlapi.model.Qrl;
import com.qrlapi.qrlapi.service.QrlService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = {"/api"})
public class QrlController {

    private final QrlService qrlService;

    @Autowired
    public QrlController(final QrlService qrlService) {
        this.qrlService = qrlService;
    }

    @CrossOrigin
    @GetMapping(path = {"/urls"}, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> urls() throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        return ResponseEntity.
                ok(mapper.writeValueAsString(qrlService.getAllQrls()));
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping(path = {"/urls"}, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addUrl(@RequestBody final Qrl qrl) {
        final ResponseEntity.BodyBuilder badRequest =
                ResponseEntity.status(HttpStatus.BAD_REQUEST);

        final String stamp = qrl.getStamp();

        if (StringUtils.isEmpty(qrl.getUrl())) {
            return badRequest.body("{\"message\":\"Url is empty :v\"}");
        }

        if (qrlService.findQrlByStamp(stamp) != null) {
            return badRequest.body("{\"message\":\"Stamp taken!\"}");
        }

        qrlService.addQrl(qrl);
        final String response = qrlService.createJson(qrl);

        return ResponseEntity.ok(response);
    }

    @CrossOrigin
    @GetMapping(path = {"/urls/{stamp}"})
    public ResponseEntity<?> link(@PathVariable final String stamp) {
        final Qrl qrl = qrlService.findQrlByStamp(stamp);

        if (qrl == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        final String response = qrlService.generateLink(qrl.getUrl());
        qrlService.removeQrl(qrl);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = {"/purge"})
    public void purge() {
        qrlService.purge();
    }
}
