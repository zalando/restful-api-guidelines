package de.zalando.zally.rule;

import de.zalando.zally.apireview.RestApiBaseTest;
import de.zalando.zally.dto.RuleDTO;
import de.zalando.zally.dto.ViolationType;
import de.zalando.zally.util.ErrorResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@TestPropertySource(properties = "zally.ignoreRules=M001,S001,C001")
public class RestSupportedRulesTest extends RestApiBaseTest {

    private static final List<String> IGNORED_RULES = Arrays.asList("M001", "S001", "C001");

    @Autowired
    private List<Rule> implementedRules;

    @Test
    public void testRulesCount() {
        assertThat(getSupportedRules().size()).isEqualTo(implementedRules.size());
    }

    @Test
    public void testRulesFieldsAreNotNull() {
        for (RuleDTO rule : getSupportedRules()) {
            assertThat(rule.getCode()).as("Rule: " + rule.getTitle()).isNotNull();
        }
    }

    @Test
    public void testIsActiveFlag() {
        for (RuleDTO rule : getSupportedRules()) {
            assertThat(rule.getActive()).isEqualTo(!IGNORED_RULES.contains(rule.getCode()));
        }
    }

    @Test
    public void testFilterByType() {
        for (ViolationType ruleType : ViolationType.values()) {
            assertFilteredByRuleType(ruleType.toString());
            assertFilteredByRuleType(ruleType.toString().toLowerCase());
        }
    }

    @Test
    public void testReturnsForUnknownType() {
        ResponseEntity<ErrorResponse> response = getSupportedRules("TOPKEK", null, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getHeaders().getContentType().toString()).isEqualTo(APPLICATION_PROBLEM_JSON);
        assertThat(response.getBody().getTitle()).isEqualTo(BAD_REQUEST.getReasonPhrase());
        assertThat(response.getBody().getStatus()).isNotEmpty();
        assertThat(response.getBody().getDetail()).isNotEmpty();
    }

    @Test
    public void testFilterByActiveTrue() {
        List<RuleDTO> rules = getSupportedRules(null, true);
        assertThat(rules.size()).isEqualTo(implementedRules.size() - IGNORED_RULES.size());
    }

    @Test
    public void testFilterByActiveFalse() {
        List<RuleDTO> rules = getSupportedRules(null, false);
        assertThat(rules.size()).isEqualTo(IGNORED_RULES.size());
    }

    private void assertFilteredByRuleType(String ruleType) throws AssertionError {
        List<RuleDTO> rules = getSupportedRules(ruleType, null);
        List<Rule> expectedRules = getRulesByType(ViolationType.valueOf(ruleType.toUpperCase()));

        assertThat(rules.size()).isEqualTo(expectedRules.size());
    }

    private List<Rule> getRulesByType(ViolationType violationType) {
        return implementedRules
            .stream()
            .filter(r -> r.getViolationType() == violationType)
            .collect(Collectors.toList());
    }
}
