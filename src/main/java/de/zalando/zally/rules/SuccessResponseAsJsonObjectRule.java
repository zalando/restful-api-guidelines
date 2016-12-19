package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SuccessResponseAsJsonObjectRule implements Rule {
    private static final String TITLE = "Response As JSON Object";
    private static final String DESCRIPTION = "Always Return JSON Objects As Top-Level Data Structures To Support Extensibility";
    private static final ViolationType VIOLATION_TYPE = ViolationType.MUST;
    private static final String RULE_LINK = "https://zalando.github.io/restful-api-guidelines/compatibility/Compatibility.html#must-always-return-json-objects-as-toplevel-data-structures-to-support-extensibility";

    @Override
    public List<Violation> validate(Swagger swagger) {
        List<Violation> violations = new ArrayList();
        if (swagger != null && swagger.getPaths() != null) {
            swagger.getPaths().entrySet().stream().
                    forEach(path -> path.getValue().getOperationMap().entrySet().stream().
                            forEach(op -> op.getValue().getResponses().entrySet().stream().
                                    filter(response -> response.getKey().startsWith("2")).
                                    filter(response -> response.getValue().getSchema() != null).
                                    filter(response -> !"ref".equals(response.getValue().getSchema().getType())).
                                    forEach(response -> {
                                        String violationPath = path.getKey() + "/" + op.getKey() + "/" + response.getKey();
                                        violations.add(new Violation(TITLE, DESCRIPTION, VIOLATION_TYPE, RULE_LINK, violationPath));
                                    })));

        }
        return violations;
    }

}
