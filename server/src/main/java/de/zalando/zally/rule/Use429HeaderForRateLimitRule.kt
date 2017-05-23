package de.zalando.zally.rule

import de.zalando.zally.violation.Violation
import de.zalando.zally.violation.ViolationType
import io.swagger.models.Swagger
import io.swagger.models.properties.Property
import org.springframework.stereotype.Component

@Component
class Use429HeaderForRateLimitRule : AbstractRule() {
    override val title = "Use 429 With Header For Rate Limits"
    override val url = "http://zalando.github.io/restful-api-guidelines/http/Http.html" +
        "#must-use-429-with-headers-for-rate-limits"
    override val violationType = ViolationType.MUST
    override val code = "M014"
    private val DESCRIPTION = "If Client Exceed Request Rate, Response Code Must Contain Header Information Providing Further Details to Client"
    private val X_RATE_LIMIT_TRIO = listOf("X-RateLimit-Limit", "X-RateLimit-Remaining", "X-RateLimit-Reset")

    override fun validate(swagger: Swagger): Violation? {
        val paths = swagger.paths.orEmpty().flatMap { (path, pathObj) ->
            pathObj.operationMap.orEmpty().entries.flatMap { (verb, operation) ->
                operation.responses.orEmpty().flatMap { (code, response) ->
                    if (code == "429" && !containsRateLimitHeader(response.headers.orEmpty()))
                        listOf("$path $verb $code")
                    else emptyList()
                }
            }
        }
        return if (paths.isNotEmpty())
            Violation(this, title, DESCRIPTION, violationType, url, paths)
        else null
    }

    private fun containsRateLimitHeader(headers: Map<String, Property>): Boolean =
        headers.containsKey("Retry-After") || headers.keys.containsAll(X_RATE_LIMIT_TRIO)
}
