package com.qrlapi.qrlapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qrlapi.qrlapi.dao.QrlRepository;
import com.qrlapi.qrlapi.model.Qrl;
import com.qrlapi.qrlapi.util.QrlTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static com.qrlapi.qrlapi.util.QrlTestUtils.expectedGetAllQrls;
import static com.qrlapi.qrlapi.util.QrlTestUtils.qrls;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QrlControllerTest {

    @Autowired
    private QrlRepository qrlRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void before() {
        qrlRepository.deleteAll();
        qrlRepository.saveAll(qrls());
    }

    @Test
    public void shouldGetAllQrls() {
        // given
        final HttpStatus expectedStatus = HttpStatus.OK;
        final String expectedBody = expectedGetAllQrls();

        // when
        final ResponseEntity<String> qrls = restTemplate
                .getForEntity(QrlTestUtils.URL, String.class);

        // then
        final HttpStatus actualStatus = qrls.getStatusCode();
        final String actualBody = qrls.getBody();

        assertEquals(expectedStatus, actualStatus);
        assertEquals(expectedBody, actualBody);
    }

    @Test
    public void shouldAddQrlWithEmptyStamp() throws JsonProcessingException {
        // given
        final ObjectMapper mapper = new ObjectMapper();
        final Qrl qrl = Qrl.builder()
                .id(UUID.randomUUID())
                .url("https://example.com")
                .build();
        final String expectedBody = mapper.writeValueAsString(qrl);

        final HttpStatus expectedStatus = HttpStatus.OK;
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final HttpEntity<String> requestBody =
                new HttpEntity<>(expectedBody, headers);

        // when
        final ResponseEntity<String> response =
                restTemplate.postForEntity(QrlTestUtils.URL, requestBody, String.class);

        // then
        final HttpStatus actualStatus = response.getStatusCode();
        final String actualBody = response.getBody();

        assertEquals(expectedStatus, actualStatus);
        assertNotNull(expectedBody, actualBody);
    }

    @Test
    public void shouldAddQrlWithStamp() throws JsonProcessingException {
        // given
        final ObjectMapper mapper = new ObjectMapper();
        final String stamp = "testStamp";
        final Qrl qrl = Qrl.builder()
                .id(UUID.randomUUID())
                .url("https://example.com")
                .stamp(stamp)
                .build();
        final String expectedBody = mapper.writeValueAsString(qrl);

        final HttpStatus expectedStatus = HttpStatus.OK;
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final HttpEntity<String> requestBody =
                new HttpEntity<>(expectedBody, headers);

        // when
        final ResponseEntity<String> response =
                restTemplate.postForEntity(QrlTestUtils.URL, requestBody, String.class);

        // then
        final HttpStatus actualStatus = response.getStatusCode();
        final String actualBody = response.getBody();

        assertEquals(expectedStatus, actualStatus);
        assertNotNull(expectedBody, actualBody);
    }
}
