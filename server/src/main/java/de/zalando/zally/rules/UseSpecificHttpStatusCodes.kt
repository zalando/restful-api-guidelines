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

    private val allowedStatusCodes = mapOf(
            "get" to listOf("304"),
            "post" to listOf("201", "202", "207", "303"),
            "put" to listOf("201", "202", "204", "303", "409", "412", "415", "423"),
            "patch" to listOf("202", "303", "409", "412", "415", "423"),
            "delete" to listOf("202", "204", "303", "409", "412", "415", "423"),
            "all" to listOf(
                    "200", "301", "400", "401", "403", "404", "405", "406", "408", "410", "428", "429",
                    "500", "501", "503"
            )
    )

    override fun validate(swagger: Swagger): Violation? {
        return null
    }
}