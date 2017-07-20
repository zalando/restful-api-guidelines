package de.zalando.zally.apireview;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.zalando.zally.exception.MissingApiDefinitionException;
import de.zalando.zally.exception.UnaccessibleResourceUrlException;
import de.zalando.zally.rule.RulesValidator;
import de.zalando.zally.violation.ApiDefinitionRequest;
import de.zalando.zally.violation.ApiDefinitionResponse;
import de.zalando.zally.violation.Violation;
import de.zalando.zally.violation.ViolationType;
import de.zalando.zally.violation.ViolationsCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.dropwizard.DropwizardMetricServices;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

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

    @ResponseBody
    @PostMapping("/api-violations")
    public ApiDefinitionResponse validate(@RequestBody ApiDefinitionRequest request) {
        metricServices.increment("meter.api-reviews.requested");

        final String apiDefinition = retrieveApiDefinition(request);
        final List<Violation> violations = rulesValidator.validate(apiDefinition);
        apiReviewRepository.save(new ApiReview(request, apiDefinition, violations));

        ApiDefinitionResponse response = buildApiDefinitionResponse(violations);
        metricServices.increment("meter.api-reviews.processed");
        return response;
    }

    private String retrieveApiDefinition(ApiDefinitionRequest request) {
        try {
            return apiDefinitionReader.read(request);
        } catch (MissingApiDefinitionException | UnaccessibleResourceUrlException e) {
            apiReviewRepository.save(new ApiReview(request));
            throw e;
        }
    }

    private ApiDefinitionResponse buildApiDefinitionResponse(List<Violation> violations) {
        ApiDefinitionResponse response = new ApiDefinitionResponse();
        response.setMessage(message);
        response.setViolations(violations);
        response.setViolationsCount(buildViolationsCount(violations));
        return response;
    }

    private Map<String, Integer> buildViolationsCount(List<Violation> violations) {
        ViolationsCounter counter = new ViolationsCounter(violations);
        return Arrays.stream(ViolationType.values()).collect(toMap(
                violationType -> violationType.toString().toLowerCase(),
                counter::getCounter
        ));
    }
}
