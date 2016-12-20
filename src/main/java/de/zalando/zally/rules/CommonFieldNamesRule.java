package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.Model;
import io.swagger.models.Swagger;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonFieldNamesRule implements Rule {

    static final Map<String, Property> commonFields = new HashMap<>();
    static {

        commonFields.put("id", new StringProperty());
        commonFields.put("created", new StringProperty("date-time"));
        commonFields.put("modified", new StringProperty("date-time"));
        commonFields.put("type", new StringProperty());
    }

    static boolean checkCommonFields(String propName, Property property) {
        if (!commonFields.containsKey(propName)) {
            return true;
        }
        Property commonProperty = commonFields.get(propName);
        return (commonProperty.getType().equals(property.getType()) &&
                (commonProperty.getFormat() == null || commonProperty.getFormat().equals(property.getFormat())));
    }

    private final String title = "Use common field names";
    private final String link = "http://zalando.github.io/restful-api-guidelines/common-data-objects/CommonDataObjects.html" +
            "#must-use-common-field-names";

    @Override
    public List<Violation> validate(Swagger swagger) {
        List<Violation> violations = new ArrayList<>();

        if (swagger.getDefinitions() == null) {
            return violations;
        }

        for (Model definition : swagger.getDefinitions().values()) {
            for (Map.Entry<String, Property> entry: definition.getProperties().entrySet()) {
                String name = entry.getKey();
                Property property = entry.getValue();
                if (!checkCommonFields(name, property)) {
                    String description = "Common field " + name + " in definition " + definition.getTitle() + " was of type " + property.getType() + ". Expected type " + commonFields.get(name);
                    violations.add(new Violation(title, description, ViolationType.MUST, link));
                }
            }
        }

        return violations;
    }
}
