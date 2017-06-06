package de.zalando.zally.apireview;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.zalando.zally.exception.MissingApiDefinitionException;
import de.zalando.zally.exception.UnaccessibleResourceUrlException;
import de.zalando.zally.rule.RulesValidator;
import de.zalando.zally.violation.Violation;
import de.zalando.zally.violation.ViolationType;
import de.zalando.zally.violation.ViolationsCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.dropwizard.DropwizardMetricServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
public class ApiViolationsController {

    private final RulesValidator rulesValidator;
    private final ObjectMapper mapper;
    private final DropwizardMetricServices metricServices;
    private final ApiDefinitionReader apiDefinitionReader;
    private final ApiReviewRepository apiReviewRepository;
    private final String message;

    @Autowired
    public ApiViolationsController(RulesValidator rulesValidator,
                                   ObjectMapper objectMapper,
                                   DropwizardMetricServices metricServices,
                                   ApiDefinitionReader apiDefinitionReader,
                                   ApiReviewRepository apiReviewRepository,
                                   @Value("${zally.message:}") String message) {
        this.rulesValidator = rulesValidator;
        this.mapper = objectMapper;
        this.metricServices = metricServices;
        this.apiDefinitionReader = apiDefinitionReader;
        this.apiReviewRepository = apiReviewRepository;
        this.message = message;
    }

    @PostMapping("/api-violations")
    public ResponseEntity<JsonNode> validate(@RequestBody JsonNode request) {
        metricServices.increment("meter.api-reviews.requested");

        final String apiDefinition = retrieveApiDefinition(request);
        final List<Violation> violations = rulesValidator.validate(apiDefinition);
        apiReviewRepository.save(new ApiReview(request, apiDefinition, violations));

        ObjectNode response = mapper.createObjectNode();
        if (message != null && !message.isEmpty()) {
            response.put("message", message);
        }

        ArrayNode jsonViolations = response.putArray("violations");
        violations.forEach(jsonViolations::addPOJO);

        ObjectNode violationsCount = response.putObject("violations_count");
        setCounters(violations, violationsCount);

        metricServices.increment("meter.api-reviews.processed");
        return ResponseEntity.ok(response);
    }

    private String retrieveApiDefinition(JsonNode request) {
        try {
            return apiDefinitionReader.read(request);
        } catch (MissingApiDefinitionException | UnaccessibleResourceUrlException e) {
            apiReviewRepository.save(new ApiReview(request));
            throw e;
        }
    }

    private void setCounters(List<Violation> violations, ObjectNode result) {
        ViolationsCounter counter = new ViolationsCounter(violations);
        for (ViolationType violationType : ViolationType.values()) {
            result.put(violationType.toString().toLowerCase(), counter.getCounter(violationType));
        }
    }
}
