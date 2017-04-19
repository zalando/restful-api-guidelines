package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.PatternUtil
import de.zalando.zally.utils.getAllJsonObjects
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
class SnakeCaseInPropNameRule : AbstractRule() {
    override val title = "snake_case property names"
    override val url = "http://zalando.github.io/restful-api-guidelines/json-guidelines/JsonGuidelines.html" +
            "#must-property-names-must-be-snakecase-and-never-camelcase"
    override val violationType = ViolationType.MUST
    override val code = "M012"
    val description = "Property names must be snake_case (and never camelCase)"

    private val whitelist = setOf("_links")

    override fun validate(swagger: Swagger): Violation? {
        val result = swagger.getAllJsonObjects().flatMap { (def, path) ->
            val badProps = def.keys.filterNot { d -> PatternUtil.isSnakeCase(d) || whitelist.contains(d) }
            if (badProps.isNotEmpty()) listOf(badProps to path) else emptyList()
        }
        return if (result.isNotEmpty()) {
            val (props, paths) = result.unzip()
            val description = "Properties that are not in snake_case: " + props.flatten().toSet().joinToString(", ")
            Violation(this, title, description, violationType, url, paths)
        } else null
    }
}
