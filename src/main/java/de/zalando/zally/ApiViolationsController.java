package de.zalando.zally;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.zalando.zally.rules.RulesValidator;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/api-violations")
public class ApiViolationsController {

    private final RulesValidator rulesValidator;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public ApiViolationsController(RulesValidator rulesValidator) {
        this.rulesValidator = rulesValidator;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<JsonNode> validate(@RequestBody JsonNode request) {
        if (!request.has("api_definition")) {
            return ResponseEntity
                    .badRequest()
                    .body(mapper.createObjectNode().put("error", "no api_definition field in request json body"));
        }

        final Swagger parsedSwagger = new SwaggerParser().parse(request.get("api_definition").toString());
        if (parsedSwagger == null) {
            return ResponseEntity
                    .badRequest()
                    .body(mapper.createObjectNode().put("error", "no valid swagger definition"));
        }

        ObjectNode response = mapper.createObjectNode();
        ArrayNode jsonViolations = response.putArray("violations");
        rulesValidator.validate(parsedSwagger).forEach(jsonViolations::addPOJO);
        return ResponseEntity.ok(response);
    }
}
