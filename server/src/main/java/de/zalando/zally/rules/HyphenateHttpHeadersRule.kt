package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.PatternUtil
import org.springframework.stereotype.Component
import java.util.Optional

@Component
open class HyphenateHttpHeadersRule : HttpHeadersRule() {
    val RULE_NAME = "Use Hyphenated HTTP Headers"
    val RULE_URL = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html" +
            "#must-use-hyphenated-http-headers"

    override fun createViolation(header: String, path: Optional<String>): Violation {
        return Violation(RULE_NAME, "Header name '$header' is not hyphenated", ViolationType.MUST, RULE_URL, path)
    }

    override fun isViolation(header: String) = !PatternUtil.isHyphenated(header)
}
