package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
class NoProtocolInHostRule : AbstractRule() {
    override val title = "Host should not contain protocol"
    override val url = ""
    override val violationType = ViolationType.MUST
    val desc = "Information about protocol should be placed in schema"

    override fun validate(swagger: Swagger): Violation? {
        val host = swagger.host.orEmpty()
        return if (host.startsWith("http://") || host.startsWith("https://")) {
            Violation(this, title, desc, violationType, url, emptyList())
        } else null
    }
}
