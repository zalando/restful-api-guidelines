package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import org.springframework.stereotype.Component
import java.util.Optional

@Component
open class AvoidLinkHeadersRule : HttpHeadersRule() {
    private val TITLE = "Avoid Link in Header Rule"
    private val DESCRIPTION = "Do Not Use Link Headers with JSON entities"
    private val RULE_LINK = "http://zalando.github.io/restful-api-guidelines/hyper-media/Hypermedia.html" +
            "#must-do-not-use-link-headers-with-json-entities"

    override fun isViolation(header: String) = header == "Link"

    override fun createViolation(header: String, path: Optional<String>): Violation {
        return Violation(TITLE, DESCRIPTION, ViolationType.MUST, RULE_LINK, path)
    }
}
