package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This validator validates a given Swagger definition based
 * on set of rules. It will sort the output by path.
 */
@Component
public class RulesValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(RulesValidator.class);

    private final List<Rule> rules;

    @Autowired
    public RulesValidator(List<Rule> rules) {
        this.rules = new LinkedList<>(rules);
    }

    public List<Violation> validate(String swaggerContent) {
        Swagger swagger = null;
        String parseErrorDescription = "";
        try {
            swagger = new SwaggerParser().parse(swaggerContent);
        } catch (Exception e) {
            parseErrorDescription = e.toString();
        }

        return swagger != null
                ? validate(swagger)
                : Collections.singletonList(new Violation("Can't parse swagger file", parseErrorDescription, ViolationType.MUST, ""));
    }

    public List<Violation> validate(Swagger swagger) {
        return rules.stream()
                .flatMap((rule -> {
                    try {
                        return rule.validate(swagger).stream();
                    } catch (Exception e) {
                        LOGGER.warn("Rule thrown an exception during validation", e);
                        return Collections.<Violation>emptyList().stream();
                    }
                }))
                .sorted((one, other) -> one.getPath().orElse("").compareToIgnoreCase(other.getPath().orElse("")))
                .collect(Collectors.toList());
    }
}
