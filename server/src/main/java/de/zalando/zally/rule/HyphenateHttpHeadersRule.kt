package de.zalando.zally.rule

import com.typesafe.config.Config
import de.zalando.zally.dto.Violation
import de.zalando.zally.dto.ViolationType
import de.zalando.zally.util.PatternUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class HyphenateHttpHeadersRule(@Autowired rulesConfig: Config) : HttpHeadersRule(rulesConfig) {
    override val title = "Use Hyphenated HTTP Headers"
    override val url = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html" +
        "#must-use-hyphenated-http-headers"
    override val violationType = ViolationType.MUST
    override val code = "M006"

    override fun isViolation(header: String) = !PatternUtil.isHyphenated(header)

    override fun createViolation(paths: List<String>): Violation {
        return Violation(this, title, "Header names should be hyphenated", violationType, url, paths)
    }
}
