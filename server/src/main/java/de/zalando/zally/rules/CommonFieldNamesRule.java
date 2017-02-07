package de.zalando.zally.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.Model;
import io.swagger.models.Swagger;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;
import org.springframework.stereotype.Component;

@Component
public class CommonFieldNamesRule implements Rule {

    private final String title = "Use common field names";
    private final String link = "http://zalando.github.io/restful-api-guidelines/common-data-objects/CommonDataObjects.html" +
            "#must-use-common-field-names";
    private final ViolationType violationType = ViolationType.MUST;

    private static final Map<String, Property> commonFields = new HashMap<>();

    static {
        commonFields.put("id", new StringProperty());
        commonFields.put("created", new StringProperty("date-time"));
        commonFields.put("modified", new StringProperty("date-time"));
        commonFields.put("type", new StringProperty());
    }

    @Override
    public List<Violation> validate(Swagger swagger) {
        List<Violation> violations = new ArrayList<>();

        if (swagger.getDefinitions() == null) {
            return violations;
        }

        for (Map.Entry<String, Model> definitionEntry : swagger.getDefinitions().entrySet()) {

            final String definition = definitionEntry.getKey();
            final Model model = definitionEntry.getValue();
            if (model.getProperties() != null) {

                for (Map.Entry<String, Property> propertyEntry : model.getProperties().entrySet()) {

                    final String name = propertyEntry.getKey();
                    final Property property = propertyEntry.getValue();

                    if (!matchesCommonFieldsType(name, property)) {
                        String description = createDescription(definition, name, "type", property.getType(), commonFields.get(name).getType());
                        violations.add(new Violation(title, description, violationType, link));
                    } else if (!matchesCommonFieldsFormat(name, property)) {
                        String description = createDescription(definition, name, "format", property.getFormat(), commonFields.get(name).getFormat());
                        violations.add(new Violation(title, description, violationType, link));
                    }
                }
            }
        }

        return violations;
    }

    static boolean matchesCommonFieldsType(String propName, Property property) {
        if (!commonFields.containsKey(propName.toLowerCase())) {
            return true;
        }
        String expectedType = commonFields.get(propName.toLowerCase()).getType();
        return expectedType == null || expectedType.equals(property.getType());
    }

    static boolean matchesCommonFieldsFormat(String propName, Property property) {
        if (!commonFields.containsKey(propName.toLowerCase())) {
            return true;
        }
        String expectedFormat = commonFields.get(propName.toLowerCase()).getFormat();
        return (property.getFormat() == null && expectedFormat == null)
                || (property.getFormat() != null && property.getFormat().equals(expectedFormat));
    }

    private String createDescription(String definition, String propertyName, String problemField, String problemValue, String expectedValue) {
        String description = "Property " + definition + "." + propertyName + " has invalid " + problemField + " " + problemValue + ". ";
        description += "Expected idiomatic " + problemField + " " + expectedValue + ".";
        return description;
    }
}
