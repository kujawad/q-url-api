package com.qurlapi.qurlapi.endpoint;

import com.qurlapi.qurlapi.dto.factory.QUrlResponseFactory;
import com.qurlapi.qurlapi.dto.request.QUrlRequest;
import com.qurlapi.qurlapi.dto.response.LinkResponse;
import com.qurlapi.qurlapi.dto.response.ProblemResponse;
import com.qurlapi.qurlapi.dto.response.QUrlResponse;
import com.qurlapi.qurlapi.model.QUrl;
import com.qurlapi.qurlapi.service.QUrlService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@AllArgsConstructor
@QUrlRestEndpoint
public class QUrlEndpoint {

    private final QUrlService qUrlService;

    @CrossOrigin
    @ApiResponses(value = {@ApiResponse(code = 200, response = QUrlResponse.class,
                                        message = "Returns list of all quick urls in database.",
                                        responseContainer = "List")})
    @GetMapping(path = {"/urls"}, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public List<QUrlResponse> urls() {
        return QUrlResponseFactory.create(qUrlService.getAllQUrls());
    }

    @CrossOrigin
    @ApiResponses(
            value = {@ApiResponse(code = 201, response = QUrlResponse.class, message = "Returns QUrlResponse object."),
                     @ApiResponse(code = 403, response = ProblemResponse.class,
                                  message = "Validation of request body failed.")})
    @PostMapping(produces = MimeTypeUtils.APPLICATION_JSON_VALUE, consumes = MimeTypeUtils.APPLICATION_JSON_VALUE,
                 path = {"/urls"})
    @ResponseStatus(value = HttpStatus.CREATED)
    public QUrlResponse addUrl(@Valid @RequestBody final QUrlRequest request) {
        final QUrl qUrl = qUrlService.addQUrl(request);
        return QUrlResponseFactory.create(qUrl);
    }

    @CrossOrigin
    @ApiResponses(
            value = {@ApiResponse(code = 200, response = LinkResponse.class, message = "Returns LinkResponse object."),
                     @ApiResponse(code = 400, response = ProblemResponse.class,
                                  message = "Bad request. Validation of path parameter failed."),
                     @ApiResponse(code = 404, response = ProblemResponse.class,
                                  message = "Not found. Stamp doesn't exists.")})
    @GetMapping(path = {"/urls/{stamp}"}, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public LinkResponse getLink(@NotNull @PathVariable final String stamp) {
        return new LinkResponse(qUrlService.useLink(stamp)
                                           .getUrl());
    }

    // TODO: JWT authentication for this endpoint
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Removes all quick urls.")})
    @ApiIgnore
    @GetMapping(path = {"/purge"})
    public void purge() {
        qUrlService.purge();
    }
}