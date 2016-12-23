package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import de.zalando.zally.utils.PatternUtil;
import io.swagger.models.Swagger;

import java.util.ArrayList;
import java.util.List;

public class KebabCaseInPathSegmentsRule implements Rule {

    String title = "Lowercase words with hyphens";
    String description = "Use lowercase separate words with hyphens for path segments";
    String ruleLink = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html" +
            "#must-use-lowercase-separate-words-with-hyphens-for-path-segments";

    public List<Violation> validate (Swagger swagger) {

        List<Violation> violations = new ArrayList<Violation>();

        if(swagger.getPaths() == null) {
            return violations;
        }

        for (String path : swagger.getPaths().keySet()) {
            for (String segment: path.split("/")){
                if(!PatternUtil.isPathVariable(segment)){
                    if(!PatternUtil.isLowerCaseAndHyphens(segment)){
                        violations.add(new Violation(title, description, ViolationType.MUST, ruleLink, path));
                        break;
                    }
                }
            }
        }

        return violations;
    }

}
