package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import de.zalando.zally.utils.PatternUtil;
import io.swagger.models.Swagger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EverySecondPathLevelParameterRule implements Rule {
    private static final String TITLE = "Every Second Path Level To Be Parameter";
    private static final String DESCRIPTION = "Every second path level must be a path parameter";
    private static final ViolationType VIOLATION_TYPE = ViolationType.MUST;
    private static final String RULE_LINK = null;

    @Override
    public List<Violation> validate(Swagger swagger) {
        List<Violation> violations = new ArrayList<>();
        if (swagger == null || swagger.getPaths() == null) {
            return violations;
        }
        for (String path : swagger.getPaths().keySet()) {
            String[] pathSegments = path.split("/");
            // we are only interested about every second element starting from the third element after split
            for (int i = 2; i < pathSegments.length; i += 2) {
                if (!PatternUtil.isPathVariable(pathSegments[i])){
                    violations.add(createViolation(path));
                    break;
                }
            }
        }
        return violations;
    }

    private Violation createViolation(String path) {
        return new Violation(TITLE, DESCRIPTION, VIOLATION_TYPE, RULE_LINK, path);
    }
}
