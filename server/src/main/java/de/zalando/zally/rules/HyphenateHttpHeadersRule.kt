package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.PatternUtil
import org.springframework.stereotype.Component

@Component
class HyphenateHttpHeadersRule : HttpHeadersRule() {
    override val title = "Use Hyphenated HTTP Headers"
    override val url = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html" +
            "#must-use-hyphenated-http-headers"
    override val violationType = ViolationType.MUST

    override fun isViolation(header: String) = !PatternUtil.isHyphenated(header)

    override fun createViolation(paths: List<String>): Violation {
        return Violation(this, title, "Header names should be hyphenated", violationType, url, paths)
    }
}
