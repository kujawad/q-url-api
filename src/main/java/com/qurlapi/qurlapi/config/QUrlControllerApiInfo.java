package com.qurlapi.qurlapi.config;

import com.qurlapi.qurlapi.dto.response.ExceptionResponse;
import com.qurlapi.qurlapi.dto.response.LinkResponse;
import com.qurlapi.qurlapi.dto.response.QUrlResponse;
import com.qurlapi.qurlapi.util.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Api(tags = {Constants.QURL_CONTROLLER_TAG})
public interface QUrlControllerApiInfo {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    response = QUrlResponse.class,
                    message = "Returns list of all quick urls in database.",
                    responseContainer = "List"
            )
    })
    @interface GetQUrlsInfo {

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    response = QUrlResponse.class,
                    message = "Returns QUrlResponse object."
            ),
            @ApiResponse(
                    code = 400,
                    response = ExceptionResponse.class,
                    message = "Bad request. Validation of request body failed. Message property may vary, depends on the circumstances."
            )
    })
    @interface AddQUrlInfo {

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    response = LinkResponse.class,
                    message = "Returns LinkResponse object."
            ),
            @ApiResponse(
                    code = 400,
                    response = ExceptionResponse.class,
                    message = "Bad request. Validation of path parameter failed."
            ),
            @ApiResponse(
                    code = 404,
                    response = ExceptionResponse.class,
                    message = "Not found. Stamp doesn't exists."
            )
    })
    @interface GetLinkInfo {

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Removes all quick urls."
            )
    })
    @ApiIgnore
    @interface PurgeInfo {

    }
}