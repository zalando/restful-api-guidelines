package de.zalando.zally;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.zalando.zally.exception.MissingApiDefinitionException;
import de.zalando.zally.exception.UnaccessibleResourceUrlException;
import de.zalando.zally.rules.RulesValidator;
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
@RestController("/api-violations")
public class ApiViolationsController {

    private final RulesValidator rulesValidator;
    private final ObjectMapper mapper;
    private final DropwizardMetricServices metricServices;
    private final ApiDefinitionReader apiDefinitionReader;
    private final ApiReviewRequestRepository apiReviewRequestRepository;
    private final String message;

    @Autowired
    public ApiViolationsController(RulesValidator rulesValidator,
                                   ObjectMapper objectMapper,
                                   DropwizardMetricServices metricServices,
                                   ApiDefinitionReader apiDefinitionReader,
                                   ApiReviewRequestRepository apiReviewRequestRepository,
                                   @Value("${zally.message:}") String message) {
        this.rulesValidator = rulesValidator;
        this.mapper = objectMapper;
        this.metricServices = metricServices;
        this.apiDefinitionReader = apiDefinitionReader;
        this.apiReviewRequestRepository = apiReviewRequestRepository;
        this.message = message;
    }

    @PostMapping
    public ResponseEntity<JsonNode> validate(@RequestBody JsonNode request) {
        metricServices.increment("meter.api-reviews.requested");

        final String apiDefinition = retrieveApiDefinition(request);
        final List<Violation> violations = rulesValidator.validate(apiDefinition);
        ObjectNode response = mapper.createObjectNode();
        if (message != null && !message.isEmpty()) {
            response.put("message", message);
        }

        ArrayNode jsonViolations = response.putArray("violations");
        violations.forEach(jsonViolations::addPOJO);

        ObjectNode violationsCount = response.putObject("violations_count");
        setCounters(violations, violationsCount);

        reportViolationMetrics(violations);
        metricServices.increment("meter.api-reviews.processed");
        return ResponseEntity.ok(response);
    }

    private String retrieveApiDefinition(JsonNode request) {
        try {
            String apiDefinition = apiDefinitionReader.read(request);
            apiReviewRequestRepository.save(new ApiReviewRequest(request.toString(), apiDefinition));
            return apiDefinition;
        } catch (MissingApiDefinitionException | UnaccessibleResourceUrlException e) {
            apiReviewRequestRepository.save(new ApiReviewRequest(request.toString()));
            throw e;
        }
    }

    private void reportViolationMetrics(List<Violation> violations) {
        violations.forEach(v -> {
            metricServices.increment("meter.api-reviews.violations.rule." + v.getRule().getName().toLowerCase());
            metricServices.increment("meter.api-reviews.violations.type." + v.getRule().getViolationType());
        });
    }

    private void setCounters(List<Violation> violations, ObjectNode result) {
        ViolationsCounter counter = new ViolationsCounter(violations);
        for (ViolationType violationType : ViolationType.values()) {
            result.put(violationType.toString().toLowerCase(), counter.getCounter(violationType));
        }
    }
}
