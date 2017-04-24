package de.zalando.zally.rules

import com.typesafe.config.Config
import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.PatternUtil
import de.zalando.zally.utils.getAllJsonObjects
import io.swagger.models.Swagger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SnakeCaseInPropNameRule(@Autowired rulesConfig: Config) : AbstractRule() {
    override val title = "snake_case property names"
    override val url = "http://zalando.github.io/restful-api-guidelines/json-guidelines/JsonGuidelines.html" +
            "#must-property-names-must-be-snakecase-and-never-camelcase"
    override val violationType = ViolationType.MUST
    override val code = "M012"
    private val description = "Property names must be snake_case: "

    private val whitelist = rulesConfig.getStringList(SnakeCaseInPropNameRule::class.simpleName + ".whitelist").toSet()

    override fun validate(swagger: Swagger): Violation? {
        val result = swagger.getAllJsonObjects().flatMap { (def, path) ->
            val badProps = def.keys.filterNot { PatternUtil.isSnakeCase(it) || whitelist.contains(it) }
            if (badProps.isNotEmpty()) listOf(badProps to path) else emptyList()
        }
        return if (result.isNotEmpty()) {
            val (props, paths) = result.unzip()
            val properties = props.flatten().toSet().joinToString(", ")
            Violation(this, title, description + properties, violationType, url, paths)
        } else null
    }
}
