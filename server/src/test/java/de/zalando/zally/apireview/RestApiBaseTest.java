package de.zalando.zally.apireview;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.zalando.zally.Application;
import de.zalando.zally.dto.ApiDefinitionRequest;
import de.zalando.zally.dto.ApiDefinitionResponse;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {Application.class, RestApiTestConfiguration.class}
)
@ActiveProfiles("test")
public abstract class RestApiBaseTest {

    public static final String API_VIOLATIONS_URL = "/api-violations";

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected ApiReviewRepository apiReviewRepository;

    @Before
    public void cleanDatabase() {
        apiReviewRepository.deleteAll();
    }

    protected ResponseEntity<JsonNode> sendRequest(JsonNode body) {
        ObjectNode requestBody = new ObjectMapper().createObjectNode();
        requestBody.set("api_definition", body);
        return restTemplate.postForEntity(
            API_VIOLATIONS_URL,
            requestBody,
            JsonNode.class);
    }

    protected final ApiDefinitionResponse sendApiViolationsRequest(ApiDefinitionRequest request) {
        ResponseEntity<ApiDefinitionResponse> entity = restTemplate.postForEntity(
                API_VIOLATIONS_URL, request, ApiDefinitionResponse.class
        );

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        return entity.getBody();
    }

    protected final ApiDefinitionResponse sendApiViolationsRequestFromResource(String resource) {
        return sendApiViolationsRequest(
                ApiDefinitionRequest.Factory.fromJsonResource(resource)
        );
    }

    protected abstract String getUrl();
}
