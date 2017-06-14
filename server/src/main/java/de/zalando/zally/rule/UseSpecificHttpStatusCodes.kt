package de.zalando.zally.rule

import com.typesafe.config.Config
import de.zalando.zally.violation.Violation
import de.zalando.zally.violation.ViolationType
import io.swagger.models.HttpMethod
import io.swagger.models.Operation
import io.swagger.models.Swagger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UseSpecificHttpStatusCodes(@Autowired rulesConfig: Config) : AbstractRule() {
    override val title = "Use Specific HTTP Status Codes"
    override val url = "http://zalando.github.io/restful-api-guidelines/http/Http.html" +
        "#must-use-specific-http-status-codes"

    // as a quick fix this rule is only SHOULD (normally MUST), see https://github.com/zalando-incubator/zally/issues/374
    override val violationType = ViolationType.SHOULD
    override val code = "M016"
    private val description = "Operatons should use specific HTTP status codes"

    private val allowedStatusCodes = rulesConfig
            .getConfig("$name.allowed_codes").entrySet()
            .map { (key, config) -> (key to config.unwrapped() as List<String>) }.toMap()

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
