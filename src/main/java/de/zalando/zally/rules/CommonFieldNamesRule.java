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

    static boolean matchesCommonFieldsType(String propName, Property property) {
        if (!commonFields.containsKey(propName)) {
            return true;
        }
        String expectedType = commonFields.get(propName).getType();
        return expectedType == null || expectedType.equals(property.getType());
    }

    static boolean matchesCommonFieldsFormat(String propName, Property property) {
        if (!commonFields.containsKey(propName)) {
            return true;
        }
        String expectedFormat = commonFields.get(propName).getFormat();
        return expectedFormat == null || expectedFormat.equals(property.getFormat());
    }

    private final String title = "Use common field names";
    private final String link = "http://zalando.github.io/restful-api-guidelines/common-data-objects/CommonDataObjects.html" +
            "#must-use-common-field-names";
    private final ViolationType violationType = ViolationType.MUST;

    @Override
    public List<Violation> validate(Swagger swagger) {
        List<Violation> violations = new ArrayList<>();

        if (swagger.getDefinitions() == null) {
            return violations;
        }

        for (Map.Entry<String, Model> definitionEntry : swagger.getDefinitions().entrySet()) {
            String definition = definitionEntry.getKey();
            Model model = definitionEntry.getValue();
            for (Map.Entry<String, Property> propertyEntry: model.getProperties().entrySet()) {
                String name = propertyEntry.getKey();
                Property property = propertyEntry.getValue();
                if (!matchesCommonFieldsType(name, property)) {
                    String description = createDescription(definition, name, "type",property.getType(), commonFields.get(name).getType());
                    violations.add(new Violation(title, description, violationType, link));
                } else if (!matchesCommonFieldsFormat(name, property)) {
                    String description = createDescription(definition, name, "format",property.getFormat(), commonFields.get(name).getFormat());
                    violations.add(new Violation(title, description, violationType, link));
                }
            }
        }

        return violations;
    }

    private String createDescription(String definition, String propertyName, String problemField, String problemValue, String expectedValue) {
        String description = "Property " + definition + "." + propertyName + " has invalid " + problemField + " " + problemValue + ". ";
        description += "Expected idiomatic " + problemField + " " + expectedValue + ".";
        return description;
    }
}
