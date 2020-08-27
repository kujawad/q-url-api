package com.qurlapi.qurlapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QUrlControllerTest {

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
        final String expectedBody = expectedGetAllQUrls();

        qUrlRepository.saveAll(qUrls());

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
    public void shouldAddQUrlWithStampNotGiven() {
        // given
        final String url = "https://example.com";
        final int usages = 3;
        final QUrl qUrl = QUrl.builder()
                .url(url)
                .usages(usages)
                .build();
        // Notice the lack of quotes in stamp request body - https://tools.ietf.org/html/rfc8259
        // (RFC 8259, section 3)
        final String requestBody = String
                .format("{\"url\":\"%s\",\"stamp\":%s,\"usages\":%d}",
                        qUrl.getUrl(), qUrl.getStamp(), qUrl.getUsages());

        final HttpStatus expectedStatus = HttpStatus.OK;
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final HttpEntity<String> request =
                new HttpEntity<>(requestBody, headers);

        // when
        final ResponseEntity<String> response =
                restTemplate.postForEntity(QUrlTestUtils.URL, request, String.class);

        // then
        final HttpStatus actualStatus = response.getStatusCode();
        final String actualBody = response.getBody();

        assertEquals(expectedStatus, actualStatus);
        assertNotNull(actualBody);

        try {
            final ObjectMapper mapper = new ObjectMapper();
            final JsonNode node = mapper.readTree(actualBody);

            assertNotNull(node.get("stamp"));
        } catch (final JsonProcessingException e) {
            fail();
        }
    }

    @Test
    public void shouldAddQUrlWithStamp() {
        // given
        final String stamp = "testStamp";
        final int usages = 3;
        final QUrl qUrl = QUrl.builder()
                .id(UUID.randomUUID())
                .url("https://example.com")
                .stamp(stamp)
                .usages(usages)
                .build();
        final String expectedBody = String
                .format("{\"url\":\"%s\",\"stamp\":\"%s\",\"usages\":%d}",
                        qUrl.getUrl(), qUrl.getStamp(), qUrl.getUsages());

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
        assertEquals(expectedBody, actualBody);
    }

    @Test
    public void shouldAddQUrlWithUsagesNotGivenAndSetUsagesToDefault() {
        // given
        final String url = "https://example.com";
        final String stamp = "stamp";
        final QUrl qUrl = QUrl.builder()
                .url(url)
                .stamp(stamp)
                .build();
        final String requestBody = String
                .format("{\"url\":\"%s\",\"stamp\":\"%s\",\"usages\":%d}",
                        qUrl.getUrl(), qUrl.getStamp(), qUrl.getUsages());

        final HttpStatus expectedStatus = HttpStatus.OK;
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final HttpEntity<String> request =
                new HttpEntity<>(requestBody, headers);

        // when
        final ResponseEntity<String> response =
                restTemplate.postForEntity(QUrlTestUtils.URL, request, String.class);

        // then
        final HttpStatus actualStatus = response.getStatusCode();
        final String actualBody = response.getBody();

        assertEquals(expectedStatus, actualStatus);
        assertNotNull(actualBody);

        try {
            final ObjectMapper mapper = new ObjectMapper();
            final JsonNode node = mapper.readTree(actualBody);
            final int actualUsages = node.get("usages").asInt();
            final int expectedUsages = 3;

            assertEquals(expectedUsages, actualUsages);
        } catch (final JsonProcessingException e) {
            fail();
        }
    }

    @Test
    public void shouldAddQUrlWithNegativeUsagesAndSetUsagesToDefault() {
        // given
        final String url = "https://example.com";
        final String stamp = "stamp";
        final int usages = -1;
        final QUrl qUrl = QUrl.builder()
                .url(url)
                .stamp(stamp)
                .usages(usages)
                .build();
        final String requestBody = String
                .format("{\"url\":\"%s\",\"stamp\":\"%s\",\"usages\":%d}",
                        qUrl.getUrl(), qUrl.getStamp(), qUrl.getUsages());

        final HttpStatus expectedStatus = HttpStatus.OK;
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final HttpEntity<String> request =
                new HttpEntity<>(requestBody, headers);

        // when
        final ResponseEntity<String> response =
                restTemplate.postForEntity(QUrlTestUtils.URL, request, String.class);

        // then
        final HttpStatus actualStatus = response.getStatusCode();
        final String actualBody = response.getBody();

        assertEquals(expectedStatus, actualStatus);
        assertNotNull(actualBody);

        try {
            final ObjectMapper mapper = new ObjectMapper();
            final JsonNode node = mapper.readTree(actualBody);
            final int actualUsages = node.get("usages").asInt();
            final int expectedUsages = 3;

            assertEquals(expectedUsages, actualUsages);
        } catch (final JsonProcessingException e) {
            fail();
        }
    }

    @Test
    public void shouldAddQrlWithMoreThanMaxUsagesAndSetToDefault() {
        // given
        final String url = "https://example.com";
        final String stamp = "stamp";
        final int usages = Integer.MAX_VALUE;
        final QUrl qUrl = QUrl.builder()
                .url(url)
                .stamp(stamp)
                .usages(usages)
                .build();
        final String requestBody = String
                .format("{\"url\":\"%s\",\"stamp\":\"%s\",\"usages\":%d}",
                        qUrl.getUrl(), qUrl.getStamp(), qUrl.getUsages());

        final HttpStatus expectedStatus = HttpStatus.OK;
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final HttpEntity<String> request =
                new HttpEntity<>(requestBody, headers);

        // when
        final ResponseEntity<String> response =
                restTemplate.postForEntity(QUrlTestUtils.URL, request, String.class);

        // then
        final HttpStatus actualStatus = response.getStatusCode();
        final String actualBody = response.getBody();

        assertEquals(expectedStatus, actualStatus);
        assertNotNull(actualBody);

        try {
            final ObjectMapper mapper = new ObjectMapper();
            final JsonNode node = mapper.readTree(actualBody);
            final int actualUsages = node.get("usages").asInt();
            final int expectedUsages = 3;

            assertEquals(expectedUsages, actualUsages);
        } catch (final JsonProcessingException e) {
            fail();
        }
    }

    @Test
    public void shouldNotAddQrlWhenUrlIsEmpty() {
        // given
        final String stamp = "stamp";
        final int usages = 3;
        final QUrl qUrl = QUrl.builder()
                .stamp(stamp)
                .usages(usages)
                .build();
        // Notice the lack of quotes in url request body - https://tools.ietf.org/html/rfc8259
        // (RFC 8259, section 3)
        final String requestBody = String
                .format("{\"url\":%s,\"stamp\":\"%s\",\"usages\":%d}",
                        qUrl.getUrl(), qUrl.getStamp(), qUrl.getUsages());

        final HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final HttpEntity<String> request =
                new HttpEntity<>(requestBody, headers);

        // when
        final ResponseEntity<String> response =
                restTemplate.postForEntity(QUrlTestUtils.URL, request, String.class);

        // then
        final HttpStatus actualStatus = response.getStatusCode();
        final String actualBody = response.getBody();

        assertEquals(expectedStatus, actualStatus);
        assertNotNull(actualBody);

        try {
            final ObjectMapper mapper = new ObjectMapper();
            final JsonNode node = mapper.readTree(actualBody);
            final String actualMessage = node.get("message").asText();
            final String expectedMessage = "Url is empty :v";

            assertEquals(expectedMessage, actualMessage);
        } catch (final JsonProcessingException e) {
            fail();
        }
    }

    @Test
    public void shouldNotAddQrlWhenStampTaken() {
        // given
        final String url = "http://example.com";
        final String stamp = "stamp";
        final int usages = 3;
        final QUrl qUrl = QUrl.builder()
                .url(url)
                .stamp(stamp)
                .usages(usages)
                .build();

        qUrlRepository.save(qUrl);

        final String requestBody = String
                .format("{\"url\":\"%s\",\"stamp\":\"%s\",\"usages\":%d}",
                        qUrl.getUrl(), qUrl.getStamp(), qUrl.getUsages());

        final HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final HttpEntity<String> request =
                new HttpEntity<>(requestBody, headers);

        // when
        final ResponseEntity<String> response =
                restTemplate.postForEntity(QUrlTestUtils.URL, request, String.class);

        // then
        final HttpStatus actualStatus = response.getStatusCode();
        final String actualBody = response.getBody();

        assertEquals(expectedStatus, actualStatus);
        assertNotNull(actualBody);

        try {
            final ObjectMapper mapper = new ObjectMapper();
            final JsonNode node = mapper.readTree(actualBody);
            final String actualMessage = node.get("message").asText();
            final String expectedMessage = "Stamp taken!";

            assertEquals(expectedMessage, actualMessage);
        } catch (final JsonProcessingException e) {
            fail();
        }
    }

    @Test
    public void shouldGenerateLink() {
        // given
        final String stamp = "stamp";
        final String url = "http://example.com";
        final int usages = 3;
        final QUrl qUrl = QUrl.builder()
                .stamp(stamp)
                .url(url)
                .usages(usages)
                .build();

        qUrlRepository.save(qUrl);

        final String expectedBody = String.format("{\"rlink\":\"%s\"}", qUrl.getUrl());
        final HttpStatus expectedStatus = HttpStatus.OK;

        // when
        final ResponseEntity<String> response =
                restTemplate.getForEntity(QUrlTestUtils.URL + "/" + stamp, String.class);

        // then
        final HttpStatus actualStatus = response.getStatusCode();
        final String actualBody = response.getBody();

        assertEquals(expectedStatus, actualStatus);
        assertEquals(expectedBody, actualBody);
    }

    @Test
    public void shouldNotGenerateLinkWhenQUrlNotFound() {
        // given
        final String stamp = "stamp";
        final HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

        // when
        final ResponseEntity<String> response =
                restTemplate.getForEntity(QUrlTestUtils.URL + "/" + stamp, String.class);

        // then
        final HttpStatus actualStatus = response.getStatusCode();

        assertEquals(expectedStatus, actualStatus);
    }
}
