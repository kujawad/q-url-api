package com.qurlapi.qurlapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qurlapi.qurlapi.dao.QUrlRepository;
import com.qurlapi.qurlapi.model.QUrl;
import com.qurlapi.qurlapi.util.QUrlTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static com.qurlapi.qurlapi.util.QUrlTestUtils.expectedGetAllQUrls;
import static com.qurlapi.qurlapi.util.QUrlTestUtils.qUrls;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QUrlControllerTest {

    @Autowired
    private QUrlController qUrlController;

    @Autowired
    private QUrlRepository qUrlRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void before() {
        qUrlRepository.deleteAll();
        qUrlRepository.saveAll(qUrls());
    }

    @Test
    public void shouldGetAllQUrls() {
        // given
        final String URL = "/api/urls";
        final HttpStatus expectedStatus = HttpStatus.OK;
        final String expectedBody = expectedGetAllQUrls();

        // when
        final ResponseEntity<String> qUrls = restTemplate
                .getForEntity(QUrlTestUtils.URL, String.class);

        // then
        final HttpStatus actualStatus = qUrls.getStatusCode();
        final String actualBody = qUrls.getBody();

        assertEquals(expectedStatus, actualStatus);
        assertEquals(expectedBody, actualBody);
    }

    @Test
    public void shouldAddQUrlWithEmptyStamp() throws JsonProcessingException {
        // given
        final ObjectMapper mapper = new ObjectMapper();
        final QUrl qUrl = QUrl.builder()
                .id(UUID.randomUUID())
                .url("https://example.com")
                .build();
        final String expectedBody = mapper.writeValueAsString(qUrl);

        final HttpStatus expectedStatus = HttpStatus.OK;
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final HttpEntity<String> requestBody =
                new HttpEntity<>(expectedBody, headers);

        // when
        final ResponseEntity<String> response =
                restTemplate.postForEntity(QUrlTestUtils.URL, requestBody, String.class);

        // then
        final HttpStatus actualStatus = response.getStatusCode();
        final String actualBody = response.getBody();

        assertEquals(expectedStatus, actualStatus);
        assertNotNull(expectedBody, actualBody);
    }

    @Test
    public void shouldAddQUrlWithStamp() throws JsonProcessingException {
        // given
        final ObjectMapper mapper = new ObjectMapper();
        final String stamp = "testStamp";
        final QUrl qUrl = QUrl.builder()
                .id(UUID.randomUUID())
                .url("https://example.com")
                .stamp(stamp)
                .build();
        final String expectedBody = mapper.writeValueAsString(qUrl);

        final HttpStatus expectedStatus = HttpStatus.OK;
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final HttpEntity<String> requestBody =
                new HttpEntity<>(expectedBody, headers);

        // when
        final ResponseEntity<String> response =
                restTemplate.postForEntity(QUrlTestUtils.URL, requestBody, String.class);

        // then
        final HttpStatus actualStatus = response.getStatusCode();
        final String actualBody = response.getBody();

        assertEquals(expectedStatus, actualStatus);
        assertNotNull(expectedBody, actualBody);
    }
}
