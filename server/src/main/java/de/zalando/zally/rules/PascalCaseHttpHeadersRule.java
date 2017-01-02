package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import de.zalando.zally.utils.PatternUtil;

import java.util.Optional;

public class PascalCaseHttpHeadersRule extends HttpHeadersRule {
    static final String RULE_NAME = "Prefer Hyphenated-Pascal-Case for HTTP header fields";
    static final String RULE_URL = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html" +
            "#should-prefer-hyphenatedpascalcase-for-http-header-fields";
    static final String DESCRIPTION = "Header name '%s' is not Hyphenated-Pascal-Case";

    @Override
    public Violation createViolation(String header, Optional<String> path) {
        if (path.isPresent()) {
            return new Violation(RULE_NAME, String.format(DESCRIPTION, header), ViolationType.SHOULD, RULE_URL, path.get());
        }
        return new Violation(RULE_NAME, String.format(DESCRIPTION, header), ViolationType.SHOULD, RULE_URL);
    }

    @Override
    public boolean isViolation(String header) {
        return !PatternUtil.isHyphenatedPascalCase(header);
    }
}
