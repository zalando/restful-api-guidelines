package de.zalando.zally.apireview;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.zalando.zally.Application;
import de.zalando.zally.statistic.ReviewStatisticRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {Application.class, RestApiTestConfiguration.class}
)
@ActiveProfiles("test")
public abstract class RestApiBaseTest {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected ApiReviewRequestRepository apiReviewRequestRepository;

    @Autowired
    protected ReviewStatisticRepository reviewStatisticRepository;

    @Before
    public void cleanDatabase() {
        reviewStatisticRepository.deleteAll();
        apiReviewRequestRepository.deleteAll();
    }

    protected ResponseEntity<JsonNode> sendRequest(JsonNode body) {
        ObjectNode requestBody = new ObjectMapper().createObjectNode();
        requestBody.set("api_definition", body);
        return restTemplate.postForEntity(
            getUrl(),
            requestBody,
            JsonNode.class);
    }

    protected abstract String getUrl();
}
