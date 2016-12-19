package de.zalando.zally;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class RestApiTest {

    @LocalServerPort
    private int port;

    private final ObjectMapper mapper = new ObjectMapper();
    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void shouldReceiveAnEmptyViolationsListForAnEmptyRequest() throws Exception {
        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/api-violations",
                mapper.createObjectNode(),
                JsonNode.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode rootObject = responseEntity.getBody();
        assertThat(rootObject.has("violations")).isTrue();

        JsonNode violations = rootObject.get("violations");
        assertThat(violations).isEmpty();
    }
}
