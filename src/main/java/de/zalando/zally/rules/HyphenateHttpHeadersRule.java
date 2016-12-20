package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import de.zalando.zally.utils.PatternUtil;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;

import java.util.*;
import java.util.stream.Collectors;

public class HyphenateHttpHeadersRule implements Rule {

    public static final String RULE_NAME = "Must: Use Hyphenated HTTP Headers";
    public static final String RULE_URL = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html" +
            "#must-use-hyphenated-http-headers";
    public static final String DESC_PATTERN = "Header name '%s' is not hyphenated";
    private static final Set<String> PARAMETER_NAMES_WHITELIST = new HashSet<>(Arrays.asList("ETag", "TSV", "TE"));

    @Override
    public List<Violation> validate(Swagger swagger) {
        List<Violation> res = new ArrayList<>();
        if (swagger.getParameters() != null) {
            res.addAll(validate(swagger.getParameters().values()));
        }
        if (swagger.getPaths() != null) {
            for (Path path : swagger.getPaths().values()) {
                res.addAll(validate(path.getParameters()));
                for (Operation operation : path.getOperations()) {
                    res.addAll(validate(operation.getParameters()));
                }
            }
        }
        return res;
    }

    private List<Violation> validate(Collection<Parameter> parameters) {
        if (parameters == null) {
            return Collections.emptyList();
        }
        return parameters
                .stream()
                .filter(p -> p.getIn().equals("header"))
                .filter(p -> !PARAMETER_NAMES_WHITELIST.contains(p.getName()) && !PatternUtil.isHyphenated(p.getName()))
                .map(this::createViolation)
                .collect(Collectors.toList());
    }

    private Violation createViolation(Parameter p) {
        return new Violation(RULE_NAME, String.format(DESC_PATTERN, p.getName()), ViolationType.MUST, RULE_URL);
    }
}
