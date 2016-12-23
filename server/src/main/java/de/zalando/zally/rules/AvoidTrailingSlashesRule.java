package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.utils.PatternUtil;
import io.swagger.models.Swagger;

import java.util.ArrayList;
import java.util.List;

import static de.zalando.zally.ViolationType.MUST;

public class AvoidTrailingSlashesRule implements Rule {
    private static final String TITLE = "Avoid Trailing Slashes";
    private static final String DESCRIPTION = "Rule avoid trailing slashes is not followed";
    private static final String RULE_LINK = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html";

    @Override
    public List<Violation> validate(Swagger swagger) {
        List<Violation> violations = new ArrayList<>();
        if (swagger != null && swagger.getPaths() != null) {
            swagger.getPaths().keySet().forEach(path -> {
                if (path != null && PatternUtil.hasTrailingSlash(path)) {
                    violations.add(new Violation(TITLE, DESCRIPTION, MUST, RULE_LINK, path));
                }
            });
        }
        return violations;
    }
}
