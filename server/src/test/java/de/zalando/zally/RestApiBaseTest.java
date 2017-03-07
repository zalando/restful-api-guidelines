package de.zalando.zally;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        webEnvironment = RANDOM_PORT,
        classes = {Application.class, RestApiTestConfiguration.class}
)
public abstract class RestApiBaseTest {

    @LocalServerPort
    protected int port;

    protected final TestRestTemplate restTemplate = new TestRestTemplate();

    protected ResponseEntity<JsonNode> sendRequest(JsonNode body) {
        ObjectNode requestBody = new ObjectMapper().createObjectNode();
        requestBody.set("api_definition", body);
        return restTemplate.postForEntity(
                getUrl(),
                requestBody,
                JsonNode.class);
    }

    protected String getUrl() {
        return "http://localhost:" + port + "/api-violations";
    }
}
