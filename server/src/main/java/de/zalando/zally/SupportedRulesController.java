package de.zalando.zally;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.zalando.zally.rules.Rule;
import de.zalando.zally.rules.RulesPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController(value = "/supported_rules")
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

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<JsonNode> listSupportedRules() {
        final ObjectNode response = objectMapper.createObjectNode();
        final ArrayNode rulesNode = response.putArray("rules");
        for (Rule rule : rules) {
            ObjectNode ruleJson = objectMapper.valueToTree(rule);
            ruleJson.put("is_active", rulesPolicy.accepts(rule));
            rulesNode.add(ruleJson);
        }

        return ResponseEntity.ok(response);
    }
}
