package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.Model;
import io.swagger.models.Swagger;
import io.swagger.models.properties.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SnakeCaseInPropNameRule implements Rule {

    //only accepts lower case and underscores
    private String pattern = "^[a-z][a-z_]*$";
    String title = "snake_case property names";
    String description = "Property names must be snake_case (and never camelCase)";
    String ruleLink = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html" +
            "#must-use-snakecase-never-camelcase-for-query-parameters";

    public List<Violation> validate(Swagger swagger) {

        List<Violation> violations = new ArrayList<Violation>();

        if (swagger.getDefinitions() == null) {
            return violations;
        }

        Map<String, Model> definitions = swagger.getDefinitions();
        for (String definitionName : definitions.keySet()) {
            Map<String, Property> properties = definitions.get(definitionName).getProperties();
            for (String propertyName : properties.keySet()) {
                if (!isSnakeCase(propertyName)) {
                    violations.add(new Violation(title, description + "\n Violating property: " + propertyName + " in definition: " + definitionName, ViolationType.MUST, ruleLink));
                }
            }
        }

        return violations;
    }

    boolean isSnakeCase(String input) {
        return input.matches(pattern);
    }
}
