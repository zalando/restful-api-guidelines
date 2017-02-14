package de.zalando.zally;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.zalando.zally.exception.MissingApiDefinitionException;
import de.zalando.zally.rules.RulesValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.dropwizard.DropwizardMetricServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/api-violations")
public class ApiViolationsController {

    private final RulesValidator rulesValidator;
    private final ObjectMapper mapper;
    private final DropwizardMetricServices metricServices;

    @Value("${zally.message}")
    private String message;

    @Autowired
    public ApiViolationsController(RulesValidator rulesValidator, ObjectMapper objectMapper,
                                   DropwizardMetricServices metricServices) {
        this.rulesValidator = rulesValidator;
        this.mapper = objectMapper;
        this.metricServices = metricServices;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<JsonNode> validate(@RequestBody JsonNode request) {
        metricServices.increment("counter.api-reviews.requested");
        if (!request.has("api_definition")) {
            throw new MissingApiDefinitionException();
        }

        final List<Violation> violations = rulesValidator.validate(request.get("api_definition").toString());
        reportViolationMetrics(violations);

        ObjectNode response = mapper.createObjectNode();
        response.put("message", message);
        ArrayNode jsonViolations = response.putArray("violations");
        violations.forEach(jsonViolations::addPOJO);

        metricServices.increment("counter.api-reviews.processed");
        return ResponseEntity.ok(response);
    }

    private void reportViolationMetrics(List<Violation> violations) {
        metricServices.submit("histogram.api-reviews.violations", violations.size());
        Arrays.stream(ViolationType.values()).forEach(v -> {
            long numberOfViolations = violations.stream().filter(violation -> violation.getViolationType() == v).count();
            metricServices.submit("histogram.api-reviews.violations." + v.getMetricIdentifier(), numberOfViolations);
        });
    }
}
