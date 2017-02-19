package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.PatternUtil
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
open class SnakeCaseInPropNameRule : Rule {
    val title = "snake_case property names"
    val description = "Property names must be snake_case (and never camelCase)"
    val ruleLink = "http://zalando.github.io/restful-api-guidelines/json-guidelines/JsonGuidelines.html" +
            "#must-property-names-must-be-snakecase-and-never-camelcase"

    override fun validate(swagger: Swagger): Violation? {
        val result = swagger.definitions.orEmpty().flatMap {
            val (definitionName, value) = it
            val badProps = value.properties.orEmpty().keys.filter { !PatternUtil.isSnakeCase(it) }
            if (badProps.isNotEmpty()) listOf("#/definitions/$definitionName" to badProps) else emptyList()
        }
        return if (result.isNotEmpty()) {
            val (paths, props) = result.unzip()
            val description = "Properties that are not in snake_case: " + props.toSet().joinToString(",")
            Violation(title, description, ViolationType.MUST, ruleLink, paths)
        } else null
    }
}
