package de.zalando.zally;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.zalando.zally.rules.RulesValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.dropwizard.DropwizardMetricServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController(value = "/api-violations")
public class ApiViolationsController {

    private final RulesValidator rulesValidator;
    private final ObjectMapper mapper;
    private final DropwizardMetricServices metricServices;
    private final String message;

    @Autowired
    public ApiViolationsController(RulesValidator rulesValidator, ObjectMapper objectMapper,
                                   DropwizardMetricServices metricServices, @Value("${zally.message:}") String message) {
        this.rulesValidator = rulesValidator;
        this.mapper = objectMapper;
        this.metricServices = metricServices;
        this.message = message;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<JsonNode> validate(@RequestBody JsonNode request) {
        metricServices.increment("counter.api-reviews.requested");

        final ApiDefinitionReader apiDefinitionReader = new ApiDefinitionReader(request);
        final List<Violation> violations = rulesValidator.validate(apiDefinitionReader.read());
        ObjectNode response = mapper.createObjectNode();
        if (message != null && !message.isEmpty()) {
            response.put("message", message);
        }

        ArrayNode jsonViolations = response.putArray("violations");
        violations.forEach(jsonViolations::addPOJO);

        ObjectNode violationsCount = response.putObject("violations_count");
        setCounters(violations, violationsCount);

        reportViolationMetrics(violations);
        metricServices.increment("counter.api-reviews.processed");
        return ResponseEntity.ok(response);
    }

    private void reportViolationMetrics(List<Violation> violations) {
        reportViolationHistograms(violations);
        reportAggregatedHistograms(violations);
    }

    private void reportViolationHistograms(List<Violation> violations) {
        violations.stream().collect(Collectors.groupingBy(Violation::getRule)).forEach((r, v) ->
                metricServices.submit("histogram.api-reviews.violations.rule." + r.getName().toLowerCase(), v.size()));
    }

    private void reportAggregatedHistograms(List<Violation> violations) {
        metricServices.submit("histogram.api-reviews.violations", violations.size());
        violations.stream().collect(Collectors.groupingBy(Violation::getViolationType)).forEach((t, v) ->
                metricServices.submit("histogram.api-reviews.violations.type." + t.getMetricIdentifier(), v.size()));
    }

    private void setCounters(List<Violation> violations, ObjectNode result) {
        ViolationsCounter counter = new ViolationsCounter(violations);
        for (ViolationType violationType : ViolationType.values()) {
            result.put(violationType.toString().toLowerCase(), counter.getCounter(violationType));
        }
    }
}
