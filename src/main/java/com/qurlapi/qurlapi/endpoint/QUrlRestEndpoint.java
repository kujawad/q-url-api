package com.qurlapi.qurlapi.endpoint;

import com.qurlapi.qurlapi.util.Constants;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RestController
@RequestMapping(path = {"/api"})
@Api(tags = {Constants.QURL_ENDPOINT_TAG})
public @interface QUrlRestEndpoint {

}