package de.zalando.zally;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.zalando.zally.rules.Rule;
import de.zalando.zally.rules.RulesPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class SupportedRulesController {

    private final List<Rule> rules;
    private final ObjectMapper objectMapper;
    private final RulesPolicy rulesPolicy;

    @Autowired
    public SupportedRulesController(List<Rule> rules, ObjectMapper objectMapper, RulesPolicy rulesPolicy) {
        this.rules = rules;
        this.objectMapper = objectMapper;
        this.rulesPolicy = rulesPolicy;
    }

    @InitBinder
    public void initBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(ViolationType.class, new ViolationTypeBinder());
    }

    @GetMapping("/supported-rules")
    public ResponseEntity<JsonNode> listSupportedRules(
            @RequestParam(value = "type", required = false) ViolationType typeFilter,
            @RequestParam(value = "is_active", required = false) Boolean isActiveFilter) {

        final ObjectNode response = objectMapper.createObjectNode();
        ArrayNode rulesNode = response.putArray("rules");
        final List<JsonNode> filteredRules = rules
                .stream()
                .filter(r -> filterByIsActive(r, isActiveFilter))
                .filter(r -> filterByType(r, typeFilter))
                .map(r -> transformRuleToObjectNode(r))
                .collect(Collectors.toList());

        rulesNode.addAll(filteredRules);

        return ResponseEntity.ok(response);
    }

    private boolean filterByIsActive(final Rule rule, final Boolean isActiveFilter) {
        final boolean isActive = rulesPolicy.accepts(rule);
        return isActiveFilter == null || isActive == isActiveFilter;
    }

    private boolean filterByType(final Rule rule, final ViolationType typeFilter) {
        return typeFilter == null || rule.getViolationType().equals(typeFilter);
    }

    private ObjectNode transformRuleToObjectNode(final Rule rule) {
        final boolean isActive = rulesPolicy.accepts(rule);
        ObjectNode ruleJson = objectMapper.valueToTree(rule);
        ruleJson.put("is_active", isActive);
        return ruleJson;
    }
}
