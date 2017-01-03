package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.Swagger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static de.zalando.zally.utils.PatternUtil.isPathVariable;

@Component
public class NestedPathsCouldBeRootPathsRule implements Rule {
    private static final String TITLE = "Consider Using (Non-) Nested URLs";
    private static final String DESCRIPTION = "Nested paths / URLs could be top-level resource";
    private static final ViolationType VIOLATION_TYPE = ViolationType.COULD;
    private static final String RULE_LINK = "http://zalando.github.io/restful-api-guidelines/resources/Resources.html" +
            "#could-consider-using-non-nested-urls";

    public List<Violation> validate(Swagger swagger) {
        List<Violation> violations = new ArrayList<>();
        if (swagger == null || swagger.getPaths() == null) {
            return violations;
        }
        for (String path : swagger.getPaths().keySet()) {
            String[] pathSegments = path.split("/");
            // we are only interested in paths that have sub-resource followed by a param: /path1/{param1}/path2/{param2}
            int length = pathSegments.length;
            if (length > 4 && isPathVariable(pathSegments[length-1])) {
                violations.add(createViolation(path));
            }
        }
        return violations;
    }
 

    private Violation createViolation(String path) {
        return new Violation(TITLE, DESCRIPTION, VIOLATION_TYPE, RULE_LINK, path);
    }
}
