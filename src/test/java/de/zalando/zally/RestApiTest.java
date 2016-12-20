package de.zalando.zally;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.zalando.zally.rules.Rule;
import de.zalando.zally.rules.RulesValidator;
import io.swagger.models.Swagger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = {Application.class, RestApiTest.TestConfiguration.class})
public class RestApiTest {

    @Configuration
    static class TestConfiguration {

        private static class CheckApiNameIsPresentRule implements Rule {

            private final String apiName;

            public CheckApiNameIsPresentRule(String apiName) {
                this.apiName = apiName;
            }

            @Override
            public List<Violation> validate(Swagger swagger) {
                if (swagger != null && swagger.getInfo().getTitle().contains(apiName)) {
                    return Arrays.asList(new Violation("dummy1", "dummy", ViolationType.MUST, "dummy"));
                } else {
                    return Collections.emptyList();
                }
            }
        }

        @Bean
        @Primary
        public RulesValidator validator() {
            return new RulesValidator(Arrays.asList(new CheckApiNameIsPresentRule("Product Service")));
        }
    }

    @LocalServerPort
    private int port;

    private final ObjectMapper mapper = new ObjectMapper();
    private final TestRestTemplate restTemplate = new TestRestTemplate();

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
    public void shouldRespondWithBadRequestWhenApiDefinitionFieldIsMissing() throws IOException {
        RequestEntity requestEntity = RequestEntity
                .post(URI.create(getUrl()))
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"my_api\": \"dummy\"}");
        ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(requestEntity, JsonNode.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldRespondWithBadRequestWhenApiDefinitionFieldIsNotValidSwaggerDefinition() throws IOException {
        RequestEntity requestEntity = RequestEntity
                .post(URI.create(getUrl()))
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"api_definition\": \"no swagger definition\"}");
        ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(requestEntity, JsonNode.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<JsonNode> sendRequest(JsonNode body) {
        ObjectNode requestBody = new ObjectMapper().createObjectNode();
        requestBody.set("api_definition", body);
        return restTemplate.postForEntity(
                getUrl(),
                requestBody,
                JsonNode.class);
    }

    private String getUrl() {
        return "http://localhost:" + port + "/api-violations";
    }
}
