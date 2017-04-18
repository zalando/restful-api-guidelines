package de.zalando.zally;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.zalando.zally.rules.Rule;
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

    @Autowired
    public SupportedRulesController(List<Rule> rules, ObjectMapper objectMapper) {
        this.rules = rules;
        this.objectMapper = objectMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<JsonNode> listSupportedRules() {
        final ObjectNode response = objectMapper.createObjectNode();
        return ResponseEntity.ok(response);
    }
}
