package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.PatternUtil
import org.springframework.stereotype.Component
import java.util.*

@Component
open class PascalCaseHttpHeadersRule : HttpHeadersRule() {
    val RULE_NAME = "Prefer Hyphenated-Pascal-Case for HTTP header fields"
    val RULE_URL = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html" +
            "#should-prefer-hyphenatedpascalcase-for-http-header-fields"

    override fun createViolation(header: String, path: Optional<String>): Violation {
        return Violation(RULE_NAME, "Header name '$header' is not Hyphenated-Pascal-Case", ViolationType.SHOULD,
                RULE_URL, path)
    }

    override fun isViolation(header: String) = !PatternUtil.isHyphenatedPascalCase(header)
}
