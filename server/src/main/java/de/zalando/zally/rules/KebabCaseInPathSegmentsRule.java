package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import de.zalando.zally.utils.PatternUtil;
import io.swagger.models.Swagger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class KebabCaseInPathSegmentsRule implements Rule {

    private final String title = "Lowercase words with hyphens";
    private final String description = "Use lowercase separate words with hyphens for path segments";
    private final String ruleLink = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html" +
            "#must-use-lowercase-separate-words-with-hyphens-for-path-segments";

    public List<Violation> validate (Swagger swagger) {

        List<Violation> violations = new ArrayList<Violation>();

        if(swagger.getPaths() == null) {
            return violations;
        }

        for (String path : swagger.getPaths().keySet()) {
            for (String segment: path.split("/")){
                if (!PatternUtil.isPathVariable(segment) && !PatternUtil.isLowerCaseAndHyphens(segment)) {
                    violations.add(new Violation(title, description, ViolationType.MUST, ruleLink, path));
                    break;
                }
            }
        }

        return violations;
    }

}
