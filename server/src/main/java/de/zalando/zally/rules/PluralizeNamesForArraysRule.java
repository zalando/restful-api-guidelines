package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.Model;
import io.swagger.models.Swagger;
import io.swagger.models.properties.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static de.zalando.zally.utils.WordUtil.isPlural;

public class PluralizeNamesForArraysRule implements Rule {

    public static final String TITLE = "Array names should be pluralized";
    public static final String DESC_PATTERN = "Array property name '%s' is not pluralized (but we are not sure)";
    public static final String RULE_URL = "http://zalando.github.io/restful-api-guidelines/json-guidelines/" +
            "JsonGuidelines.html#should-array-names-should-be-pluralized";

    @Override
    public List<Violation> validate(Swagger swagger) {
        List<Violation> res = new ArrayList<>();
        if (swagger !=null && swagger.getDefinitions() != null) {
            for (Map.Entry<String, Model> entry : swagger.getDefinitions().entrySet()) {
                String definition = entry.getKey();
                Model model = entry.getValue();
                if (model != null && model.getProperties() != null) {
                    for (Map.Entry<String, Property> e : model.getProperties().entrySet()) {
                        String name = e.getKey();
                        if ("array".equals(e.getValue().getType()) && !isPlural(name)) {
                            res.add(new Violation(
                                    TITLE,
                                    String.format(DESC_PATTERN, name),
                                    ViolationType.SHOULD, RULE_URL, definition + "." + name));
                        }
                    }
                }
            }
        }
        return res;
    }
}
