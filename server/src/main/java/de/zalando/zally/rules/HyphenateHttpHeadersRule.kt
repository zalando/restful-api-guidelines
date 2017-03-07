package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.PatternUtil
import org.springframework.stereotype.Component

@Component
open class HyphenateHttpHeadersRule : HttpHeadersRule() {
    val RULE_NAME = "Use Hyphenated HTTP Headers"
    val RULE_URL = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html" +
            "#must-use-hyphenated-http-headers"

    override fun isViolation(header: String) = !PatternUtil.isHyphenated(header)

    override fun createViolation(paths: List<String>): Violation {
        return Violation(this, RULE_NAME, "Header names should be hyphenated", ViolationType.MUST, RULE_URL, paths)
    }
}
