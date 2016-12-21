package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import de.zalando.zally.utils.PatternUtil;
import io.swagger.models.Swagger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static de.zalando.zally.utils.WordUtil.isPlural;

public class PluralizeResourceNamesRule implements Rule {
    public static final String RULE_NAME = "Pluralize Resource Names";
    public static final String RULE_URL = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html" +
            "#must-pluralize-resource-names";
    public static final String DESC_PATTERN = "Resource name '%s' is not pluralized (but we are not sure)";

    @Override
    public List<Violation> validate(Swagger swagger) {
        List<Violation> res = new ArrayList<>();
        if (swagger.getPaths() != null) {
            for (String path : swagger.getPaths().keySet()) {
                res.addAll(Arrays
                        .stream(path.split("/"))
                        .filter(s -> !s.isEmpty() && !PatternUtil.isPathVariable(s))
                        .filter(s -> !isPlural(s))
                        .map(s -> createViolation(path, s))
                        .collect(Collectors.toList()));
            }
        }
        return res;
    }

    private Violation createViolation(String path, String resource) {
        return new Violation(RULE_NAME, String.format(DESC_PATTERN, resource), ViolationType.SHOULD, RULE_URL, path);
    }
}
