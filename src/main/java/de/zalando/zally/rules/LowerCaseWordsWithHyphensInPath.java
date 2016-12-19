package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.Swagger;

import java.util.ArrayList;
import java.util.List;

public class LowerCaseWordsWithHyphensInPath implements Rule {

    //only accepts lower case and hyphens
    private String pattern = "^[a-z-]*$";
    String title = "lowercase words with hyphens";
    String description = "Use lowercase separate words with hyphens for path segments";
    String ruleLink = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html";

    public List<Violation> validate (Swagger swagger) {

        List<Violation> violations = new ArrayList<Violation>();

        if(swagger.getPaths() == null) {
            return violations;
        }

        for (String path : swagger.getPaths().keySet()) {
            for (String segment: path.split("/")){
                if(!isPathVariable(segment)){
                    if(!isLowerCaseAndHyphens(segment)){
                        violations.add(new Violation(title, description, ViolationType.MUST, ruleLink, path));
                        break;
                    }
                }
            }
        }

        return violations;
    }

    boolean isPathVariable(String input ) {
        return input.startsWith("{") && input.endsWith("}");
    }

    boolean isLowerCaseAndHyphens(String input){
        return input.matches(pattern);
    }

}
