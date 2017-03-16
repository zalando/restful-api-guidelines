package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import org.springframework.stereotype.Component

@Component
class AvoidLinkHeadersRule : HttpHeadersRule() {
    override val title = "Avoid Link in Header Rule"
    override val violationType = ViolationType.MUST
    override val url = "http://zalando.github.io/restful-api-guidelines/hyper-media/Hypermedia.html" +
            "#must-do-not-use-link-headers-with-json-entities"
    private val DESCRIPTION = "Do Not Use Link Headers with JSON entities"

    override fun isViolation(header: String) = header == "Link"

    override fun createViolation(paths: List<String>): Violation {
        return Violation(this, title, DESCRIPTION, violationType, url, paths)
    }
}
