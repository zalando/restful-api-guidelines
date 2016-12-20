package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AvoidLinkHeadersRule implements Rule {
    private static final String TITLE = "Avoid Link in Header Rule";
    private static final String DESCRIPTION = "Do Not Use Link Headers with JSON entities";
    private static final ViolationType VIOLATION_TYPE = ViolationType.MUST;
    private static final String RULE_LINK = "http://zalando.github.io/restful-api-guidelines/hyper-media/Hypermedia.html#must-do-not-use-link-headers-with-json-entities";

    @Override
    public List<Violation> validate(Swagger swagger) {
        List<Violation> violations = new ArrayList<>();
        if (swagger != null && swagger.getPaths() != null) {
            for (Map.Entry<String, Path> path : swagger.getPaths().entrySet())
                for (Map.Entry<HttpMethod, Operation> op : path.getValue().getOperationMap().entrySet()) {
                    for (Parameter param : op.getValue().getParameters()) {
                        if ("header".equals(param.getIn()) && "Link".equals(param.getName())) {
                            String violationPath = path.getKey() + "/" + op.getKey() + "/" + param.getName();
                            violations.add(createViolation(violationPath));
                        }
                    }
                    for (Map.Entry<String, Response> response : op.getValue().getResponses().entrySet()) {
                        if (response.getValue().getHeaders() != null && response.getValue().getHeaders().containsKey("Link")) {
                            String violationPath = path.getKey() + "/" + op.getKey() + "/" + response.getKey();
                            violations.add(createViolation(violationPath));
                        }
                    }

                }

        }
        return violations;
    }

    private Violation createViolation(String path) {
        return new Violation(TITLE, DESCRIPTION, VIOLATION_TYPE, RULE_LINK, path);
    }

}
