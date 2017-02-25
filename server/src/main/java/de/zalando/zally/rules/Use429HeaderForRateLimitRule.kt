package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Swagger
import io.swagger.models.properties.Property
import org.springframework.stereotype.Component
import java.util.*


@Component
open class Use429HeaderForRateLimitRule : Rule {
    private val TITLE = "Use 429 With Header For Rate Limits"
    private val DESCRIPTION = "If Client Exceed Request Rate, Response Code Must Contain Header Information Providing Further Details to Client"
    private val RULE_LINK = "http://zalando.github.io/restful-api-guidelines/http/Http.html" +
            "#must-use-429-with-headers-for-rate-limits"
    private val X_RATE_LIMIT_TRIO = Arrays.asList("X-RateLimit-Limit", "X-RateLimit-Remaining", "X-RateLimit-Reset")

    override fun validate(swagger: Swagger): Violation? {
        val paths = swagger.paths.orEmpty().flatMap {
            val (path, pathObj) = it
            pathObj.operationMap.orEmpty().entries.flatMap {
                val (verb, operation) = it
                operation.responses.orEmpty().flatMap {
                    val (code, response) = it
                    if (code == "429" && !containsRateLimitHeader(response.headers.orEmpty()))
                        listOf("$path $verb $code")
                    else emptyList()
                }
            }
        }
        return if (paths.isNotEmpty())
            Violation(TITLE, DESCRIPTION, ViolationType.MUST, RULE_LINK, paths)
        else null
    }

    private fun containsRateLimitHeader(headers: Map<String, Property>): Boolean =
            headers.containsKey("Retry-After") || headers.keys.containsAll(X_RATE_LIMIT_TRIO)
}
