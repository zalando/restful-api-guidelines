package de.zalando.zally;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.zalando.zally.rules.RulesValidator;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController(value = "/api-violations")
public class ApiViolationsController {

    private final RulesValidator rulesValidator;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public ApiViolationsController(RulesValidator rulesValidator) {
        this.rulesValidator = rulesValidator;
    }

    @RequestMapping(method = RequestMethod.POST)
    public JsonNode validate(@RequestBody JsonNode request) {
        Swagger parsedSwagger = new SwaggerParser().parse(request.get("api_definition").toString());
        final List<Violation> violations;
        if (parsedSwagger != null) {
            violations = rulesValidator.validate(parsedSwagger);
        } else {
            violations = Collections.emptyList();
        }

        ObjectNode response = mapper.createObjectNode();
        ArrayNode jsonViolations = response.putArray("violations");
        violations.forEach(jsonViolations::addPOJO);
        return response;
    }
}
