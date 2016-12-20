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


public class SuccessResponseAsJsonObjectRule implements Rule {
    private static final String TITLE = "Response As JSON Object";
    private static final String DESCRIPTION = "Always Return JSON Objects As Top-Level Data Structures To Support Extensibility";
    private static final ViolationType VIOLATION_TYPE = ViolationType.MUST;
    private static final String RULE_LINK = "https://zalando.github.io/restful-api-guidelines/compatibility/Compatibility.html#must-always-return-json-objects-as-toplevel-data-structures-to-support-extensibility";

    @Override
    public List<Violation> validate(Swagger swagger) {
        List<Violation> violations = new ArrayList<>();
        if (swagger != null && swagger.getPaths() != null) {
            for (Map.Entry<String, Path> path : swagger.getPaths().entrySet())
                for (Map.Entry<HttpMethod, Operation> op : path.getValue().getOperationMap().entrySet())
                    for(Map.Entry<String, Response> response : op.getValue().getResponses().entrySet())
                        try {
                            Integer httpCodeInt = Integer.valueOf(response.getKey());
                            if (httpCodeInt >= 200 && httpCodeInt < 300 && response.getValue().getSchema() != null && !"ref".equals(response.getValue().getSchema().getType())) {
                                String violationPath = path.getKey() + "/" + op.getKey() + "/" + response.getKey();
                                violations.add(new Violation(TITLE, DESCRIPTION, VIOLATION_TYPE, RULE_LINK, violationPath));
                            }
                        } catch(NumberFormatException nfe) {
                            // ignore any http codes which are not between 200 and 300
                        }

        }
        return violations;
    }

}
