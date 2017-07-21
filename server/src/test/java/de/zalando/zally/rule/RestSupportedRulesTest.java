package de.zalando.zally.rule;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import de.zalando.zally.apireview.RestApiBaseTest;
import de.zalando.zally.dto.ViolationType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static java.util.Collections.sort;
import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(properties = "zally.ignoreRules=M001,S001,C001")
public class RestSupportedRulesTest extends RestApiBaseTest {

    private final List<String> ignoredRules = Arrays.asList("M001", "S001", "C001");

    @Autowired
    private List<Rule> implementedRules;

    @Test
    public void shouldReturn200WhenEverythingIsOk() throws Exception {
        final ResponseEntity<JsonNode> responseEntity = sendRequest(SUPPORTED_RULES_URL);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnListOfRules() throws Exception {
        final ResponseEntity<JsonNode> responseEntity = sendRequest(SUPPORTED_RULES_URL);
        final JsonNode result = responseEntity.getBody();
        final ArrayNode rules = (ArrayNode) result.get("rules");
        assertThat(rules.size()).isEqualTo(implementedRules.size());
    }

    @Test
    public void shouldReturnProperObjectStructure() throws Exception {
        final ResponseEntity<JsonNode> responseEntity = sendRequest(SUPPORTED_RULES_URL);
        final JsonNode result = responseEntity.getBody();
        final ArrayNode rules = (ArrayNode) result.get("rules");
        final List<String> expectedFieldNames = Arrays.asList("code", "is_active", "title", "type", "url");

        for (JsonNode rule : rules) {
            final List<String> fieldNames = new ArrayList<>();
            rule.fieldNames().forEachRemaining(fieldNames::add);
            sort(fieldNames);

            assertThat(fieldNames).as("Rule: " + rule.get("title")).isEqualTo(expectedFieldNames);
        }
    }

    @Test
    public void shouldMarkRulesAsInactive() throws Exception {
        final ArrayNode rules = getRulesFromUrl(SUPPORTED_RULES_URL);

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
    public void shouldReturn400WhenTypeNotFound() throws Exception {
        final String url = SUPPORTED_RULES_URL + "?type=" + "SOLUTION";
        final ResponseEntity<JsonNode> responseEntity = sendRequest(url);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldFilterByActiveRules() throws Exception {
        final String url = SUPPORTED_RULES_URL + "?is_active=true";
        final ArrayNode rules = getRulesFromUrl(url);

        assertThat(rules.size()).isEqualTo(implementedRules.size() - ignoredRules.size());
    }

    @Test
    public void shouldFilterByInactiveRules() throws Exception {
        final String url = SUPPORTED_RULES_URL + "?is_active=false";
        final ArrayNode rules = getRulesFromUrl(url);

        assertThat(rules.size()).isEqualTo(ignoredRules.size());
    }

    private void assertFilteredByRuleType(String ruleType) throws AssertionError {
        final String url = SUPPORTED_RULES_URL + "?type=" + ruleType;
        final ArrayNode rules = getRulesFromUrl(url);
        final List<Rule> expectedRules = getRulesByType(ViolationType.valueOf(ruleType.toUpperCase()));

        assertThat(rules.size()).isEqualTo(expectedRules.size());
    }

    private ResponseEntity<JsonNode> sendRequest(String url) {
        return restTemplate.getForEntity(url, JsonNode.class);
    }

    private List<Rule> getRulesByType(final ViolationType violationType) {
        return implementedRules
            .stream()
            .filter(r -> r.getViolationType() == violationType)
            .collect(Collectors.toList());
    }

    private ArrayNode getRulesFromUrl(final String url) {
        final ResponseEntity<JsonNode> responseEntity = sendRequest(url);
        final JsonNode result = responseEntity.getBody();
        return (ArrayNode) result.get("rules");
    }
}
