package de.zalando.zally;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import de.zalando.zally.rules.Rule;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        webEnvironment = RANDOM_PORT,
        classes = {Application.class, RestApiTestConfiguration.class}
)
@ActiveProfiles("test")
@TestPropertySource(properties = "zally.ignoreRules=M001,S001,C001")
public class RestSupportedRulesTest {
    @LocalServerPort
    protected int port;

    @Autowired
    private List<Rule> implementedRules;

    private final List<String> ignoredRules = Arrays.asList("M001", "S001", "C001");

    protected final TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void shouldReturnListOfRules() throws Exception {
        final ResponseEntity<JsonNode> responseEntity = sendRequest(getUrl());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        final JsonNode result = responseEntity.getBody();
        assertThat(result.has("rules")).isTrue();

        final ArrayNode rules = (ArrayNode) result.get("rules");
        assertThat(rules.size()).isEqualTo(implementedRules.size());
    }

    @Test
    public void shouldMarkRulesAsInactive() throws Exception {
        final ArrayNode rules = getRulesFromUrl(getUrl());

        for (JsonNode rule : rules) {
            assertThat(rule.get("is_active").asBoolean()).isEqualTo(!ignoredRules.contains(rule.get("code").asText()));
        }
    }

    @Test
    public void shouldFilterByType() throws Exception {
        for (ViolationType ruleType : ViolationType.values()) {
            assertFilteredByRuleType(ruleType.toString());
            assertFilteredByRuleType(ruleType.toString().toLowerCase());
        }
    }

    @Test
    public void shouldFilterByActiveRules() throws Exception {
        final String url = getUrl() + "?is_active=true";
        final ArrayNode rules = getRulesFromUrl(url);

        assertThat(rules.size()).isEqualTo(implementedRules.size() - ignoredRules.size());
    }

    @Test
    public void shouldFilterByInactiveRules() throws Exception {
        final String url = getUrl() + "?is_active=false";
        final ArrayNode rules = getRulesFromUrl(url);

        assertThat(rules.size()).isEqualTo(ignoredRules.size());
    }

    private void assertFilteredByRuleType(String ruleType) throws AssertionError {
        final String url = getUrl() + "?type=" + ruleType;
        final ArrayNode rules = getRulesFromUrl(url);
        final List<Rule> expectedRules = getRulesByType(ViolationType.valueOf(ruleType.toUpperCase()));

        assertThat(rules.size()).isEqualTo(expectedRules.size());
    }

    protected ResponseEntity<JsonNode> sendRequest(String url) {
        return restTemplate.getForEntity(url, JsonNode.class);
    }

    protected String getUrl() {
        return "http://localhost:" + port + "/supported-rules";
    }

    protected List<Rule> getRulesByType(final ViolationType violationType) {
        return implementedRules
                .stream()
                .filter(r -> r.getViolationType() == violationType)
                .collect(Collectors.toList());
    }

    protected ArrayNode getRulesFromUrl(final String url) {
        final ResponseEntity<JsonNode> responseEntity = sendRequest(url);
        final JsonNode result = responseEntity.getBody();
        return (ArrayNode) result.get("rules");
    }
}
