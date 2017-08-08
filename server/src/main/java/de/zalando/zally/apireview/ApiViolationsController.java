package de.zalando.zally.apireview;

import de.zalando.zally.dto.ApiDefinitionRequest;
import de.zalando.zally.dto.ApiDefinitionResponse;
import de.zalando.zally.dto.ViolationDTO;
import de.zalando.zally.dto.ViolationType;
import de.zalando.zally.dto.ViolationsCounter;
import de.zalando.zally.exception.MissingApiDefinitionException;
import de.zalando.zally.exception.UnaccessibleResourceUrlException;
import de.zalando.zally.rule.ApiValidator;
import de.zalando.zally.rule.Violation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.dropwizard.DropwizardMetricServices;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@CrossOrigin
@RestController
public class ApiViolationsController {

    private final ApiValidator rulesValidator;
    private final DropwizardMetricServices metricServices;
    private final ApiDefinitionReader apiDefinitionReader;
    private final ApiReviewRepository apiReviewRepository;
    private final ServerMessageService serverMessageService;

    @Autowired
    public ApiViolationsController(ApiValidator rulesValidator,
                                   DropwizardMetricServices metricServices,
                                   ApiDefinitionReader apiDefinitionReader,
                                   ApiReviewRepository apiReviewRepository,
                                   ServerMessageService serverMessageService) {
        this.rulesValidator = rulesValidator;
        this.metricServices = metricServices;
        this.apiDefinitionReader = apiDefinitionReader;
        this.apiReviewRepository = apiReviewRepository;
        this.serverMessageService = serverMessageService;
    }

    @ResponseBody
    @PostMapping("/api-violations")
    public ApiDefinitionResponse validate(@RequestBody ApiDefinitionRequest request,
                                          @RequestHeader(value = "User-Agent", required = false) String userAgent) {
        metricServices.increment("meter.api-reviews.requested");

        String apiDefinition = retrieveApiDefinition(request);
        List<Violation> violations = rulesValidator.validate(apiDefinition, request.getIgnoreRules());
        apiReviewRepository.save(new ApiReview(request, apiDefinition, violations));

        ApiDefinitionResponse response = buildApiDefinitionResponse(violations, userAgent);
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

    private ApiDefinitionResponse buildApiDefinitionResponse(List<Violation> violations, String userAgent) {
        ApiDefinitionResponse response = new ApiDefinitionResponse();
        response.setMessage(serverMessageService.serverMessage(userAgent));
        response.setViolations(violations.stream().map(this::toDto).collect(toList()));
        response.setViolationsCount(buildViolationsCount(violations));
        return response;
    }

    private ViolationDTO toDto(Violation violation) {
        return new ViolationDTO(
            violation.getTitle(),
            violation.getDescription(),
            violation.getViolationType(),
            violation.getRuleLink(),
            violation.getPaths()
        );
    }

    private Map<String, Integer> buildViolationsCount(List<Violation> violations) {
        ViolationsCounter counter = new ViolationsCounter(violations);
        return Arrays.stream(ViolationType.values()).collect(toMap(
            violationType -> violationType.toString().toLowerCase(),
            counter::getCounter
        ));
    }
}
