package de.zalando.zally;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.zalando.zally.exception.MissingApiDefinitionException;
import de.zalando.zally.rules.RulesValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/api-violations")
public class ApiViolationsController {

    private final RulesValidator rulesValidator;
    private final ObjectMapper mapper;

    @Autowired
    public ApiViolationsController(RulesValidator rulesValidator, ObjectMapper objectMapper) {
        this.rulesValidator = rulesValidator;
        this.mapper = objectMapper;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<JsonNode> validate(@RequestBody JsonNode request) {
        if (!request.has("api_definition")) {
            throw new MissingApiDefinitionException();
        }

        ObjectNode response = mapper.createObjectNode();
        ArrayNode jsonViolations = response.putArray("violations");
        rulesValidator.validate(request.get("api_definition").toString()).forEach(jsonViolations::addPOJO);
        return ResponseEntity.ok(response);
    }
}
