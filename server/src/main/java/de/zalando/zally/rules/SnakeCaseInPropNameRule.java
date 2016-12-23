package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import de.zalando.zally.utils.PatternUtil;
import io.swagger.models.Model;
import io.swagger.models.Swagger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SnakeCaseInPropNameRule implements Rule {

    String title = "snake_case property names";
    String description = "Property names must be snake_case (and never camelCase)";
    String ruleLink = "http://zalando.github.io/restful-api-guidelines/json-guidelines/JsonGuidelines.html" +
            "#must-property-names-must-be-snakecase-and-never-camelcase";

    public List<Violation> validate(Swagger swagger) {
        List<Violation> violations = new ArrayList<>();
        if (swagger.getDefinitions() != null) {
            Map<String, Model> definitions = swagger.getDefinitions();
            for (Map.Entry<String, Model> entry : definitions.entrySet()) {
                String definitionName = entry.getKey();
                if(entry.getValue().getProperties() != null) {
                    for (String propertyName : entry.getValue().getProperties().keySet()) {
                        if (!PatternUtil.isSnakeCase(propertyName)) {
                            violations.add(new Violation(title, description + "\n Violating property: " + propertyName + " in definition: " + definitionName, ViolationType.MUST, ruleLink));
                        }
                    }
                }
            }
        }
        return violations;
    }
}
