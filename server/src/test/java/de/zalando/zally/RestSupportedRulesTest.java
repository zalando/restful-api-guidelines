package de.zalando.zally;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import de.zalando.zally.rules.Rule;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        webEnvironment = RANDOM_PORT,
        classes = {Application.class, RestApiTestConfiguration.class}
)
@ActiveProfiles("test")
public class RestSupportedRulesTest {
    @LocalServerPort
    protected int port;

    @Autowired
    private List<Rule> implementedRules;

    protected final TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void shouldReturnListOfRules() throws Exception {
        ResponseEntity<JsonNode> responseEntity = sendRequest();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        final JsonNode result = responseEntity.getBody();
        assertThat(result.has("rules")).isTrue();

        final ArrayNode rules = (ArrayNode) result.get("rules");
        assertThat(rules.size()).isEqualTo(implementedRules.size());
    }


    protected ResponseEntity<JsonNode> sendRequest() {
        return restTemplate.getForEntity(getUrl(), JsonNode.class);
    }

    protected String getUrl() {
        return "http://localhost:" + port + "/supported-rules";
    }
}
