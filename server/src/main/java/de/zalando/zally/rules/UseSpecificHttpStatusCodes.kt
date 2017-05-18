package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
class UseSpecificHttpStatusCodes : AbstractRule() {
    override val title = "Use Specific HTTP Status Codes"
    override val url = "http://zalando.github.io/restful-api-guidelines/http/Http.html" +
        "#must-use-specific-http-status-codes"
    override val violationType = ViolationType.MUST
    override val code = "M016"

    override fun validate(swagger: Swagger): Violation? {
        return null
    }
}