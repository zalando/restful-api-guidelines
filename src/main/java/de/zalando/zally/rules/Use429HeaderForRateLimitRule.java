package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.Response;
import io.swagger.models.properties.Property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class Use429HeaderForRateLimitRule implements Rule {
    private static final String TITLE = "Use 429 With Header For Rate Limits";
    private static final String DESCRIPTION = "If Client Exceed Request Rate, Response Code Must Contain Header Information Providing Further Details to Client";
    private static final ViolationType VIOLATION_TYPE = ViolationType.MUST;
    private static final String RULE_LINK = "http://zalando.github.io/restful-api-guidelines/http/Http.html#must-use-429-with-headers-for-rate-limits";
    private static final List<String> X_RATE_LIMIT_TRIO = Arrays.asList("X-RateLimit-Limit", "X-RateLimit-Remaining", "X-RateLimit-Reset") ;

    @Override
    public List<Violation> validate(Swagger swagger) {
        List<Violation> violations = new ArrayList<>();
        if (swagger == null || swagger.getPaths() == null) {
            return violations;
        }
        for(Map.Entry<String, Path> path: swagger.getPaths().entrySet())
            for(Map.Entry<HttpMethod, Operation> op : path.getValue().getOperationMap().entrySet())
                for(Map.Entry<String, Response> response: op.getValue().getResponses().entrySet())
                    if ("429".equals(response.getKey()) && !validateLimitHeader(response.getValue().getHeaders())) {
                        String violationPath = path.getKey() + "/" + op.getKey() + "/" + response.getKey();
                        violations.add(new Violation(TITLE, DESCRIPTION, VIOLATION_TYPE, RULE_LINK, violationPath));
                    }
        return violations;
    }

    private boolean validateLimitHeader(Map<String, Property> headers) {
        return headers != null && (headers.containsKey("Retry-After") || headers.keySet().containsAll(X_RATE_LIMIT_TRIO));
    }
}
