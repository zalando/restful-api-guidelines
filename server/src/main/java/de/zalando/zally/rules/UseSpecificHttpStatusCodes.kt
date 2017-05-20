package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.HttpMethod
import io.swagger.models.Operation
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
class UseSpecificHttpStatusCodes : AbstractRule() {
    override val title = "Use Specific HTTP Status Codes"
    override val url = "http://zalando.github.io/restful-api-guidelines/http/Http.html" +
        "#must-use-specific-http-status-codes"
    override val violationType = ViolationType.MUST
    override val code = "M016"
    private val description = "Operatons should use specific HTTP status codes"

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
        val badPaths = swagger.paths.orEmpty().flatMap { path ->
            path.value.operationMap.orEmpty().flatMap { getNotAllowedStatusCodes(path.key, it) }
        }
        return if (badPaths.isNotEmpty()) Violation(this, title, description, violationType, url, badPaths) else null
    }

    private fun getNotAllowedStatusCodes(path: String, entry: Map.Entry<HttpMethod, Operation>): List<String> {
        val statusCodes = entry.value.responses.orEmpty().keys.toList()
        val allowedCodes = getAllowedStatusCodes(entry.key)
        val notAllowedCodes = statusCodes.filter { !allowedCodes.contains(it) }
        return notAllowedCodes.map { "$path ${entry.key.name} $it" }
    }

    private fun getAllowedStatusCodes(httpMethod: HttpMethod): List<String> {
        return allowedStatusCodes.getOrDefault(httpMethod.name.toLowerCase(), emptyList()) +
                allowedStatusCodes.getOrDefault("all", emptyList())
    }
}