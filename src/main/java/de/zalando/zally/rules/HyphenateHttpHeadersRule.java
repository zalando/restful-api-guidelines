package de.zalando.zally.rules;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import de.zalando.zally.utils.PatternUtil;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;

import java.util.*;
import java.util.stream.Collectors;

public class HyphenateHttpHeadersRule extends HttpHeadersRule {

    static final String RULE_NAME = "Must: Use Hyphenated HTTP Headers";
    static final String RULE_URL = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html" +
            "#must-use-hyphenated-http-headers";
    static final String DESC_PATTERN = "Header name '%s' is not hyphenated";

    @Override
    Violation createViolation(String header, Optional<String> path) {
        if (path.isPresent()) {
            return new Violation(RULE_NAME, String.format(DESC_PATTERN, header), ViolationType.MUST, RULE_URL, path.get());
        }
        return new Violation(RULE_NAME, String.format(DESC_PATTERN, header), ViolationType.MUST, RULE_URL);
    }

    @Override
    boolean isViolation(String header) {
        return !PatternUtil.isHyphenated(header);
    }
}
