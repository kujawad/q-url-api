package com.qurlapi.qurlapi.controller;

import com.qurlapi.qurlapi.assembler.dto.QUrlRequestAssembler;
import com.qurlapi.qurlapi.assembler.model.QUrlAssembler;
import com.qurlapi.qurlapi.dao.QUrlRepository;
import com.qurlapi.qurlapi.dto.request.QUrlRequest;
import com.qurlapi.qurlapi.dto.response.ExceptionResponse;
import com.qurlapi.qurlapi.dto.response.LinkResponse;
import com.qurlapi.qurlapi.dto.response.QUrlResponse;
import com.qurlapi.qurlapi.model.QUrl;
import com.qurlapi.qurlapi.util.QUrlIT;
import com.qurlapi.qurlapi.util.QUrlTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QUrlIT
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QUrlControllerIT {

    @Autowired
    private QUrlRepository qUrlRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void before() {
        qUrlRepository.deleteAll();
    }

    @Test
    public void shouldGetAllQUrls() {
        // given
        final HttpStatus expectedStatus = HttpStatus.OK;
        final List<QUrl> qUrls = List.of(QUrlAssembler.any(), QUrlAssembler.any());
        qUrlRepository.saveAll(qUrls);

        // when
        final ResponseEntity<List<QUrlResponse>> response = restTemplate.exchange(QUrlTestUtils.URL, HttpMethod.GET,
                                                                                  null,
                                                                                  new ParameterizedTypeReference<>() {
                                                                                  });

        // then
        final HttpStatus actualStatus = response.getStatusCode();
        final List<QUrlResponse> actualBody = response.getBody();

        assertThat(actualStatus).isEqualTo(expectedStatus);
        assertThat(actualBody).isNotNull();
        assertThat(actualBody.size()).isEqualTo(qUrls.size());
    }

    @Test
    public void shouldAddQUrlWithStampNotGiven() {
        // given
        final QUrlRequest requestBody = QUrlRequestAssembler.make()
                                                            .withStamp(null)
                                                            .assemble();

        final HttpStatus expectedStatus = HttpStatus.OK;
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final HttpEntity<QUrlRequest> request = new HttpEntity<>(requestBody, headers);

        // when
        final ResponseEntity<QUrlResponse> response = restTemplate.postForEntity(QUrlTestUtils.URL, request,
                                                                                 QUrlResponse.class);

        // then
        final HttpStatus actualStatus = response.getStatusCode();
        final QUrlResponse actualBody = response.getBody();

        assertThat(actualStatus).isEqualTo(expectedStatus);
        assertThat(actualBody).isNotNull();
        assertThat(actualBody.getStamp()).isNotNull();
    }

    @Test
    public void shouldAddQUrl() {
        // given
        final HttpStatus expectedStatus = HttpStatus.OK;
        final QUrlRequest requestBody = QUrlRequestAssembler.any();

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<QUrlRequest> request = new HttpEntity<>(requestBody, headers);

        // when
        final ResponseEntity<QUrlResponse> response = restTemplate.postForEntity(QUrlTestUtils.URL, request,
                                                                                 QUrlResponse.class);

        // then
        final HttpStatus actualStatus = response.getStatusCode();
        final QUrlResponse actualBody = response.getBody();

        assertThat(actualStatus).isEqualTo(expectedStatus);
        assertThat(actualBody).isNotNull();
        assertThat(actualBody.getStamp()).isEqualTo(requestBody.getStamp());
    }

    @Test
    public void shouldNotAddQUrlWithUsagesNotGiven() {
        // given
        final HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        final QUrlRequest requestBody = QUrlRequestAssembler.make()
                                                            .withUsages(null)
                                                            .assemble();

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<QUrlRequest> request = new HttpEntity<>(requestBody, headers);

        // when
        final ResponseEntity<ExceptionResponse> response = restTemplate.postForEntity(QUrlTestUtils.URL, request,
                                                                                      ExceptionResponse.class);
        // then
        final HttpStatus actualStatus = response.getStatusCode();
        final ExceptionResponse actualBody = response.getBody();

        assertThat(actualStatus).isEqualTo(expectedStatus);
        assertThat(actualBody).isNotNull();
        assertThat(actualBody.getMessage()).isEqualTo("must not be null");
    }

    @Test
    public void shouldNotAddQrlWithMoreThanMaxUsages() {
        // given
        final QUrlRequest requestBody = QUrlRequestAssembler.make()
                                                            .withUsages(Integer.MAX_VALUE)
                                                            .assemble();

        final HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final HttpEntity<QUrlRequest> request = new HttpEntity<>(requestBody, headers);

        // when
        final ResponseEntity<ExceptionResponse> response = restTemplate.postForEntity(QUrlTestUtils.URL, request,
                                                                                      ExceptionResponse.class);
        // then
        final HttpStatus actualStatus = response.getStatusCode();
        final ExceptionResponse actualBody = response.getBody();

        assertThat(actualStatus).isEqualTo(expectedStatus);
        assertThat(actualBody).isNotNull();
        assertThat(actualBody.getMessage()).isEqualTo("Usage values must be lower or equal to 128");
    }

    @Test
    public void shouldNotAddQUrlWithNegativeUsages() {
        // given
        final HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        final QUrlRequest requestBody = QUrlRequestAssembler.make()
                                                            .withUsages(-1)
                                                            .assemble();

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<QUrlRequest> request = new HttpEntity<>(requestBody, headers);

        // when
        final ResponseEntity<ExceptionResponse> response = restTemplate.postForEntity(QUrlTestUtils.URL, request,
                                                                                      ExceptionResponse.class);
        // then
        final HttpStatus actualStatus = response.getStatusCode();
        final ExceptionResponse actualBody = response.getBody();

        assertThat(actualStatus).isEqualTo(expectedStatus);
        assertThat(actualBody).isNotNull();
        assertThat(actualBody.getMessage()).isEqualTo("Usage values must be higher or equal to 1");
    }

    @Test
    public void shouldNotAddQrlWhenUrlIsEmpty() {
        // given
        final HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        final String expectedMessage = "Url is empty :v";
        final QUrlRequest requestBody = QUrlRequestAssembler.make()
                                                            .withUrl("")
                                                            .assemble();

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<QUrlRequest> request = new HttpEntity<>(requestBody, headers);

        // when
        final ResponseEntity<ExceptionResponse> response = restTemplate.postForEntity(QUrlTestUtils.URL, request,
                                                                                      ExceptionResponse.class);

        // then
        final ExceptionResponse actualBody = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(expectedStatus);
        assertThat(actualBody).isNotNull();
        assertThat(actualBody.getMessage()).isEqualTo(expectedMessage);
    }

    @Test
    public void shouldNotAddQrlWhenUrlIsNull() {
        // given
        final HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        final String expectedMessage = "Url is empty :v";
        final int expectedRepositorySize = 0;
        final QUrlRequest requestBody = QUrlRequestAssembler.make()
                                                            .withUrl(null)
                                                            .assemble();

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<QUrlRequest> request = new HttpEntity<>(requestBody, headers);

        // when
        final ResponseEntity<ExceptionResponse> response = restTemplate.postForEntity(QUrlTestUtils.URL, request,
                                                                                      ExceptionResponse.class);

        // then
        final ExceptionResponse actualBody = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(expectedStatus);
        assertThat(actualBody).isNotNull();
        assertThat(actualBody.getMessage()).isEqualTo(expectedMessage);
        assertThat(qUrlRepository.findAll()
                                 .size()).isEqualTo(expectedRepositorySize);
    }

    @Test
    public void shouldNotAddQrlWhenStampTaken() {
        // given
        final HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        final String expectedMessage = "Stamp taken!";
        final int expectedRepositorySize = 1;
        final QUrl qUrl = QUrlAssembler.any();
        qUrlRepository.save(qUrl);
        final QUrlRequest requestBody = QUrlRequestAssembler.make()
                                                            .withStamp(qUrl.getStamp())
                                                            .assemble();

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<QUrlRequest> request = new HttpEntity<>(requestBody, headers);

        // when
        final ResponseEntity<ExceptionResponse> response = restTemplate.postForEntity(QUrlTestUtils.URL, request,
                                                                                      ExceptionResponse.class);

        // then
        final ExceptionResponse actualBody = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(expectedStatus);
        assertThat(actualBody).isNotNull();
        assertThat(actualBody.getMessage()).isEqualTo(expectedMessage);
        assertThat(qUrlRepository.findAll()
                                 .size()).isEqualTo(expectedRepositorySize);
    }

    @Test
    public void shouldGenerateLink() {
        // given
        final HttpStatus expectedStatus = HttpStatus.OK;
        final QUrl qUrl = QUrlAssembler.any();
        qUrlRepository.save(qUrl);

        // when
        final ResponseEntity<LinkResponse> response = restTemplate.getForEntity(
                QUrlTestUtils.URL + "/" + qUrl.getStamp(),
                LinkResponse.class);

        // then
        final HttpStatus actualStatus = response.getStatusCode();
        final LinkResponse actualBody = response.getBody();

        assertThat(actualStatus).isEqualTo(expectedStatus);
        assertThat(actualBody).isNotNull();
        assertThat(actualBody.getRlink()).isEqualTo(qUrl.getUrl());
    }

    @Test
    public void shouldNotGenerateLinkWhenQUrlNotFound() {
        // given
        final String stamp = "stamp";
        final HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

        // when
        final ResponseEntity<String> response = restTemplate.getForEntity(QUrlTestUtils.URL + "/" + stamp,
                                                                          String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(expectedStatus);
    }
}
