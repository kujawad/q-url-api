package com.qurlapi.qurlapi.endpoint;

import com.qurlapi.qurlapi.dto.factory.QUrlResponseFactory;
import com.qurlapi.qurlapi.dto.request.QUrlRequest;
import com.qurlapi.qurlapi.dto.response.LinkResponse;
import com.qurlapi.qurlapi.dto.response.ProblemResponse;
import com.qurlapi.qurlapi.dto.response.QUrlResponse;
import com.qurlapi.qurlapi.model.QUrl;
import com.qurlapi.qurlapi.service.QUrlService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@AllArgsConstructor
@QUrlRestEndpoint
public class QUrlEndpoint {

    private final QUrlService qUrlService;

    @CrossOrigin
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true, description = "Returns list of all quick urls.")
    @GetMapping(path = {"/urls"}, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public List<QUrlResponse> urls() {
        return QUrlResponseFactory.create(qUrlService.getAllQUrls());
    }

    @CrossOrigin
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", useReturnTypeSchema = true, description = "Returns QUrlResponse object"),
            @ApiResponse(responseCode = "403", description = "Validation of request body failed.",
                         content = @Content(schema = @Schema(implementation = ProblemResponse.class)))})
    @PostMapping(produces = MimeTypeUtils.APPLICATION_JSON_VALUE, consumes = MimeTypeUtils.APPLICATION_JSON_VALUE,
                 path = {"/urls"})
    @ResponseStatus(value = HttpStatus.CREATED)
    public QUrlResponse addUrl(@Valid @RequestBody final QUrlRequest request) {
        final QUrl qUrl = qUrlService.addQUrl(request);
        return QUrlResponseFactory.create(qUrl);
    }

    @CrossOrigin
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true, description = "Returns LinkResponse object"),

            @ApiResponse(responseCode = "400",
                         content = @Content(schema = @Schema(implementation = ProblemResponse.class)),
                         description = "Bad request. Validation of path parameter failed."),
            @ApiResponse(responseCode = "404",
                         content = @Content(schema = @Schema(implementation = ProblemResponse.class)),
                         description = "Not found. Stamp doesn't exists.")})
    @GetMapping(path = {"/urls/{stamp}"}, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public LinkResponse getLink(@NotNull @PathVariable final String stamp) {
        return new LinkResponse(qUrlService.useLink(stamp)
                                           .getUrl());
    }

    // TODO: JWT authentication for this endpoint
    @Hidden
    @GetMapping(path = {"/purge"})
    public void purge() {
        qUrlService.purge();
    }
}