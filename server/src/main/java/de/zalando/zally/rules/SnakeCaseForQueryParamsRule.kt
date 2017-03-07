package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.PatternUtil
import io.swagger.models.Swagger
import io.swagger.models.parameters.QueryParameter
import org.springframework.stereotype.Component

/**
 * Lint for snake case for query params
 */
@Component
open class SnakeCaseForQueryParamsRule : AbstractRule() {
    val TITLE = "Use snake_case (never camelCase) for Query Parameters"
    val RULE_LINK = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html" +
            "#must-use-snakecase-never-camelcase-for-query-parameters"

    override fun validate(swagger: Swagger): Violation? {
        val result = swagger.paths.orEmpty().flatMap {
            val (path, pathObject) = it
            pathObject.operationMap.orEmpty().flatMap {
                val (verb, operation) = it
                val badParams = operation.parameters.filter { it is QueryParameter && !PatternUtil.isSnakeCase(it.name) }
                if (badParams.isNotEmpty()) listOf("$path $verb" to badParams) else emptyList()
            }
        }
        return if (result.isNotEmpty()) {
            val (paths, params) = result.unzip()
            val description = "Parameters that are not in snake_case: " + params.flatten().map { it.name }.toSet().joinToString(",")
            Violation(this, TITLE, description, ViolationType.MUST, RULE_LINK, paths)
        } else null
    }
}
