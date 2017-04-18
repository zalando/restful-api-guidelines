package de.zalando.zally;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        webEnvironment = RANDOM_PORT,
        classes = {Application.class, RestApiTestConfiguration.class}
)
@ActiveProfiles("test")
public class RestSupportedRulesTest {
    @LocalServerPort
    protected int port;

    protected final TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void shouldReturnOk() throws Exception {
        ResponseEntity<JsonNode> responseEntity = sendRequest();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    protected ResponseEntity<JsonNode> sendRequest() {
        return restTemplate.getForEntity(getUrl(), JsonNode.class);
    }

    protected String getUrl() {
        return "http://localhost:" + port + "/supported-rules";
    }
}
