package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
class NoProtocolInHostRule : AbstractRule() {
    override val title = "Host should not contain protocol"
    // TODO: Provide URL
    override val url = ""
    override val violationType = ViolationType.MUST
    override val code = "M008"
    private val desc = "Information about protocol should be placed in schema. Current host value '%s' violates this rule"

    override fun validate(swagger: Swagger): Violation? {
        val host = swagger.host.orEmpty()
        return if ("://" in host)
            Violation(this, title, desc.format(host), violationType, url, emptyList())
        else null
    }
}
