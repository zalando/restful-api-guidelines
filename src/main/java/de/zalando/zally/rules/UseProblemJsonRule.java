package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Swagger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class UseProblemJsonRule implements Rule {
    private static final String TITLE = "Use Problem JSON";
    private static final String DESCRIPTION = "Operations Should Return Problem JSON When Any Problem Occurs During Processing Whether Caused by Client Or Server";
    private static final ViolationType VIOLATION_TYPE = ViolationType.MUST;
    private static final String RULE_LINK = "https://zalando.github.io/restful-api-guidelines/compatibility/Compatibility.html#must-use-problem-json";

    @Override
    public List<Violation> validate(Swagger swagger) {
        List<Violation> violations = new ArrayList<>();
        if (swagger == null || swagger.getPaths() == null) {
            return violations;
        }
        for(Map.Entry<String, Path> path: swagger.getPaths().entrySet())
            for(Map.Entry<HttpMethod, Operation> op : path.getValue().getOperationMap().entrySet())
                for(Map.Entry<String, Response> response: op.getValue().getResponses().entrySet()) {
                    Integer httpCodeInt = null;
                    try {
                        httpCodeInt = Integer.valueOf(response.getKey());
                    } catch (NumberFormatException nfe) {
                        // ignore any http codes which are not between 400 and 599
                    }
                    //TODO: check if schema reference type is Problem := https://github.com/swagger-api/swagger-parser/pull/303/files
                    if (httpCodeInt != null && httpCodeInt >= 400 && httpCodeInt <= 599
                            && response.getValue().getSchema() != null
                            && !"ref".equals(response.getValue().getSchema().getType())) {
                        String violationPath = path.getKey() + "/" + op.getKey() + "/" + response.getKey();
                        violations.add(new Violation(TITLE, DESCRIPTION, VIOLATION_TYPE, RULE_LINK, violationPath));
                    }
                }
        return violations;
    }

}
