package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.PatternUtil
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
class SnakeCaseInPropNameRule : AbstractRule() {
    override val title = "snake_case property names"
    override val url = "http://zalando.github.io/restful-api-guidelines/json-guidelines/JsonGuidelines.html" +
            "#must-property-names-must-be-snakecase-and-never-camelcase"
    override val violationType = ViolationType.MUST
    val description = "Property names must be snake_case (and never camelCase)"

    override fun validate(swagger: Swagger): Violation? {
        val result = swagger.definitions.orEmpty().flatMap { (definitionName, value) ->
            val badProps = value.properties.orEmpty().keys.filterNot(PatternUtil::isSnakeCase)
            if (badProps.isNotEmpty()) listOf("#/definitions/$definitionName" to badProps) else emptyList()
        }
        return if (result.isNotEmpty()) {
            val (paths, props) = result.unzip()
            val description = "Properties that are not in snake_case: " + props.flatten().toSet().joinToString(", ")
            Violation(this, title, description, violationType, url, paths)
        } else null
    }
}
