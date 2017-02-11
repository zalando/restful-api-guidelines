package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.PatternUtil
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
open class SnakeCaseInPropNameRule : Rule {

    val title = "snake_case property names"
    val description = "Property %s is not in camel_case"
    val ruleLink = "http://zalando.github.io/restful-api-guidelines/json-guidelines/JsonGuidelines.html" +
            "#must-property-names-must-be-snakecase-and-never-camelcase"

    private val whitelist = setOf("_links")

    override fun validate(swagger: Swagger): List<Violation> {
        val definitions = swagger.definitions ?: emptyMap()
        return definitions.flatMap { entry ->
            val definition = entry.key
            val props = entry.value?.properties ?: emptyMap()
            props.keys.filter { !PatternUtil.isSnakeCase(it) && !whitelist.contains(it) }
                    .map { Violation(title, String.format(description, it), ViolationType.MUST, ruleLink, definition) }
        }
    }
}
