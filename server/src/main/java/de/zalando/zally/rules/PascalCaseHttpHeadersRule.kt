package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.PatternUtil
import org.springframework.stereotype.Component

@Component
class PascalCaseHttpHeadersRule : HttpHeadersRule() {
    override val title = "Prefer Hyphenated-Pascal-Case for HTTP header fields"
    override val url = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html" +
            "#should-prefer-hyphenatedpascalcase-for-http-header-fields"
    override val violationType = ViolationType.SHOULD

    override fun isViolation(header: String) = !PatternUtil.isHyphenatedPascalCase(header)

    override fun createViolation(paths: List<String>): Violation {
        return Violation(this, title, "Header is not Hyphenated-Pascal-Case", violationType, url, paths)
    }
}
