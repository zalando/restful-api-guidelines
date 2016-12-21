package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import de.zalando.zally.utils.Inflector;
import de.zalando.zally.utils.PatternUtil;
import io.swagger.models.Swagger;

import java.util.*;
import java.util.stream.Collectors;

public class PluralizeResourceNamesRule implements Rule {
    private static final Inflector INFLECTOR = new Inflector();

    public static final String RULE_NAME = "Should: Pluralize Resource Names";
    public static final String RULE_URL = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html" +
            "#must-pluralize-resource-names";
    public static final String DESC_PATTERN = "Resource name '%s' is not pluralized (but we are not sure";
    private static final Set<String> PLURAL_WHITELIST = new HashSet<>(Arrays.asList("vat"));

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

    public static boolean isPlural(String resource) {
        if (PLURAL_WHITELIST.contains(resource)) {
            return true;
        }
        String singular = INFLECTOR.singularize(resource);
        String plural = INFLECTOR.pluralize(resource);
        return plural.equals(resource) && !singular.equals(resource);
    }

    private Violation createViolation(String path, String resource) {
        return new Violation(RULE_NAME, String.format(DESC_PATTERN, resource), ViolationType.SHOULD, RULE_URL, path);
    }
}
