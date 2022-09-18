package com.qurlapi.qurlapi.endpoint;

import com.qurlapi.qurlapi.assembler.dto.QUrlRequestAssembler;
import com.qurlapi.qurlapi.assembler.model.QUrlAssembler;
import com.qurlapi.qurlapi.dao.QUrlRepository;
import com.qurlapi.qurlapi.dto.request.QUrlRequest;
import com.qurlapi.qurlapi.dto.response.LinkResponse;
import com.qurlapi.qurlapi.dto.response.ProblemResponse;
import com.qurlapi.qurlapi.dto.response.QUrlResponse;
import com.qurlapi.qurlapi.exception.validation.ValidationError;
import com.qurlapi.qurlapi.model.QUrl;
import com.qurlapi.qurlapi.util.QUrlIT;
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
public class QUrlEndpointIT {

    public static final String URL = "/api/urls";

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
        final List<QUrl> qUrls = List.of(QUrlAssembler.any(), QUrlAssembler.any());
        qUrlRepository.saveAll(qUrls);

        // when
        final ResponseEntity<List<QUrlResponse>> response = restTemplate.exchange(URL, HttpMethod.GET, null,
                                                                                  new ParameterizedTypeReference<>() {
                                                                                  });

        // then
        final List<QUrlResponse> actualBody = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualBody).isNotNull();
        assertThat(actualBody.size()).isEqualTo(qUrls.size());
    }

    @Test
    public void shouldAddQUrl() {
        // given
        final QUrlRequest request = QUrlRequestAssembler.any();

        // when
        final ResponseEntity<QUrlResponse> response = post(request, QUrlResponse.class);

        // then
        final QUrlResponse actualBody = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(actualBody).isNotNull();
        assertThat(actualBody.getStamp()).isEqualTo(request.getStamp());
    }

    @Test
    public void shouldAddQUrlWithStampNotGiven() {
        // given
        final QUrlRequest request = QUrlRequestAssembler.make()
                                                        .withStamp(null)
                                                        .assemble();
        // when
        final ResponseEntity<QUrlResponse> response = post(request, QUrlResponse.class);

        // then
        final QUrlResponse actualBody = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(actualBody).isNotNull();
        assertThat(actualBody.getStamp()).isNotNull();
    }

    @Test
    public void shouldNotAddQrlWithMoreThanMaxUsages() {
        // given
        final QUrlRequest request = QUrlRequestAssembler.make()
                                                        .withUsages(129)
                                                        .assemble();

        // when
        final ResponseEntity<ProblemResponse> response = post(request, ProblemResponse.class);

        // then
        final ProblemResponse actualBody = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(actualBody).isNotNull();
        assertThat(actualBody.getTitle()).isEqualTo("Request parameters are not valid");
        assertThat(actualBody.getMessage()).isEqualTo(
                "Validation failed for object 'QUrlRequest': field errors (1) on ['usages'(1)]");
        assertThat(actualBody.getValidationErrors()).isNotEmpty();
        final ValidationError validationError = actualBody.getValidationErrors()
                                                          .get(0);
        assertThat(validationError.getMessageKey()).isEqualTo("max");
        assertThat(validationError.getContextKey()).isEqualTo("usages");
        assertThat(validationError.getMessage()).isEqualTo("Usage values must be lower or equal to 128");
    }

    @Test
    //TODO: add more asserts
    public void shouldNotAddQUrlWithNegativeUsages() {
        // given
        final QUrlRequest request = QUrlRequestAssembler.make()
                                                        .withUsages(-1)
                                                        .assemble();

        // when
        final ResponseEntity<ProblemResponse> response = post(request, ProblemResponse.class);

        // then
        final ProblemResponse actualBody = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(actualBody).isNotNull();
        assertThat(actualBody.getMessage()).isEqualTo("Validation failed for object 'QUrlRequest': field errors (1) on ['usages'(1)]");
    }

    @Test
    //TODO: add more asserts
    public void shouldNotAddQrlWhenUrlIsEmpty() {
        // given
        final QUrlRequest request = QUrlRequestAssembler.make()
                                                        .withUrl("")
                                                        .assemble();

        // when
        final ResponseEntity<ProblemResponse> response = post(request, ProblemResponse.class);

        // then
        final ProblemResponse actualBody = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(actualBody).isNotNull();
        assertThat(actualBody.getMessage()).isEqualTo("Validation failed for object 'QUrlRequest': field errors (1) on ['url'(1)]");
    }

    @Test
    //TODO: add more asserts
    public void shouldNotAddQrlWhenUrlIsNull() {
        // given
        final QUrlRequest request = QUrlRequestAssembler.make()
                                                        .withUrl(null)
                                                        .assemble();

        // when
        final ResponseEntity<ProblemResponse> response = post(request, ProblemResponse.class);

        // then
        final ProblemResponse actualBody = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(actualBody).isNotNull();
        assertThat(actualBody.getMessage()).isEqualTo("Validation failed for object 'QUrlRequest': field errors (1) on ['url'(1)]");
        assertThat(qUrlRepository.findAll()
                                 .size()).isEqualTo(0);
    }

    @Test
    //TODO: add more asserts
    public void shouldNotAddQrlWhenStampTaken() {
        // given
        final QUrl qUrl = QUrlAssembler.any();
        qUrlRepository.save(qUrl);
        final QUrlRequest request = QUrlRequestAssembler.make()
                                                        .withStamp(qUrl.getStamp())
                                                        .assemble();

        // when
        final ResponseEntity<ProblemResponse> response = post(request, ProblemResponse.class);

        // then
        final ProblemResponse actualBody = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(actualBody).isNotNull();
        assertThat(actualBody.getMessage()).isEqualTo(
                "QUrl with stamp identifier " + qUrl.getStamp() + " already exists");
        assertThat(qUrlRepository.findAll()
                                 .size()).isEqualTo(1);
    }

    @Test
    public void shouldGenerateLink() {
        // given
        final QUrl qUrl = QUrlAssembler.any();
        qUrlRepository.save(qUrl);

        // when
        final ResponseEntity<LinkResponse> response = get(URL + "/" + qUrl.getStamp() + "/generate", LinkResponse.class);

        // then
        final HttpStatus actualStatus = response.getStatusCode();
        final LinkResponse actualBody = response.getBody();

        assertThat(actualStatus).isEqualTo(HttpStatus.OK);
        assertThat(actualBody).isNotNull();
        assertThat(actualBody.getRlink()).isEqualTo(qUrl.getUrl());
    }

    @Test
    public void shouldNotGenerateLinkWhenQUrlNotFound() {
        // given
        final String stamp = "stamp";
        final HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

        // when
        final ResponseEntity<String> response = get(URL + "/" + stamp + "/generate", String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(expectedStatus);
    }

    private <T> ResponseEntity<T> get(final String url, final Class<T> response) {
        return restTemplate.getForEntity(url, response);
    }

    private <T> ResponseEntity<T> post(final Object request, final Class<T> response) {
        return restTemplate.postForEntity(URL, prepareRequest(request), response);
    }

    private <T> HttpEntity<T> prepareRequest(final T body) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }
}
