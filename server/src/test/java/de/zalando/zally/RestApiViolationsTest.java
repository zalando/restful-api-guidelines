package de.zalando.zally;

import java.io.IOException;
import java.net.URI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.zalando.zally.exception.MissingApiDefinitionException;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.ResourceUtils;

import static org.assertj.core.api.Assertions.assertThat;


@TestPropertySource(properties = "zally.message=Test message")
public class RestApiViolationsTest extends RestApiBaseTest {

    @Test
    public void shouldValidateGivenApiDefinition() throws IOException {
        ResponseEntity<JsonNode> responseEntity = sendRequest(
                new ObjectMapper().readTree(ResourceUtils.getFile("src/test/resources/api_spp.json")));
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode rootObject = responseEntity.getBody();
        assertThat(rootObject.has("violations")).isTrue();

        JsonNode violations = rootObject.get("violations");
        assertThat(violations).hasSize(1);
        assertThat(violations.get(0).get("title").asText()).isEqualTo("dummy1");

        String message = rootObject.get("message").asText();
        assertThat(message).isEqualTo("Test message");
    }

    @Test
    public void shouldReturnMetricsOfFoundViolations() throws IOException {
        ResponseEntity<JsonNode> responseEntity = sendRequest(
                new ObjectMapper().readTree(ResourceUtils.getFile("src/test/resources/api_spp.json")));
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<JsonNode> metricsResponse = restTemplate.getForEntity("http://localhost:" + port + "/metrics", JsonNode.class);
        assertThat(metricsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode rootObject = metricsResponse.getBody();
        assertThat(rootObject.has("counter.api-reviews.requested")).isTrue();
        assertThat(rootObject.has("counter.api-reviews.processed")).isTrue();
        assertThat(rootObject.has("histogram.api-reviews.violations.count")).isTrue();
        assertThat(rootObject.has("histogram.api-reviews.violations.must.count")).isTrue();
        assertThat(rootObject.has("histogram.api-reviews.violations.should.count")).isTrue();
        assertThat(rootObject.has("histogram.api-reviews.violations.could.count")).isTrue();
        assertThat(rootObject.has("histogram.api-reviews.violations.hint.count")).isTrue();
    }

    @Test
    public void shouldRespondWithBadRequestOnMalformedJson() throws IOException {
        RequestEntity requestEntity = RequestEntity
                .post(URI.create(getUrl()))
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"malformed\": \"dummy\"");
        ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(requestEntity, JsonNode.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldRespondWithProblemJsonOnMalformedJson() throws IOException {
        RequestEntity requestEntity = RequestEntity
                .post(URI.create(getUrl()))
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"malformed\": \"dummy\"");

        ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(requestEntity, JsonNode.class);

        assertThat(responseEntity.getHeaders().getContentType().toString()).isEqualTo("application/problem+json");
        assertThat(responseEntity.getBody().has("title")).isTrue();
        assertThat(responseEntity.getBody().has("status")).isTrue();
        assertThat(responseEntity.getBody().has("detail")).isTrue();
    }

    @Test
    public void shouldRespondWithBadRequestWhenApiDefinitionFieldIsMissing() throws IOException {
        RequestEntity requestEntity = RequestEntity
                .post(URI.create(getUrl()))
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"my_api\": \"dummy\"}");

        ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(requestEntity, JsonNode.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().get("title").asText()).isEqualTo("Bad Request");
        assertThat(responseEntity.getBody().get("detail").asText()).isEqualTo(MissingApiDefinitionException.MESSAGE);
    }

    @Test
    public void shouldRespondWithViolationWhenApiDefinitionFieldIsNotValidSwaggerDefinition() throws IOException {
        RequestEntity requestEntity = RequestEntity
                .post(URI.create(getUrl()))
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"api_definition\": \"no swagger definition\"}");

        ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(requestEntity, JsonNode.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode rootObject = responseEntity.getBody();
        assertThat(rootObject.has("violations")).isTrue();

        JsonNode violations = rootObject.get("violations");
        assertThat(violations).hasSize(1);
        assertThat(violations.get(0).get("title").asText()).isEqualTo("Can't parse swagger file");
    }
}
