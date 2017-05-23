package de.zalando.zally.rule

import de.zalando.zally.util.PatternUtil
import de.zalando.zally.util.WordUtil.isPlural
import de.zalando.zally.violation.Violation
import de.zalando.zally.violation.ViolationType
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
class PluralizeResourceNamesRule : AbstractRule() {
    override val title = "Pluralize Resource Names"
    override val url = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html" +
        "#must-pluralize-resource-names"
    override val violationType = ViolationType.SHOULD
    override val code = "S008"
    private val DESC_PATTERN = "Resources %s are singular (but we are not sure)"

    override fun validate(swagger: Swagger): Violation? {
        val res = swagger.paths.keys.flatMap { path ->
            path.split("/".toRegex()).filter { s -> !s.isEmpty() && !PatternUtil.isPathVariable(s) && !isPlural(s) }
                .map { it to path }
        }
        return if (res.isNotEmpty()) {
            val desc = res.map { "'${it.first}'" }.toSet().joinToString(", ")
            val paths = res.map { it.second }
            Violation(this, title, String.format(DESC_PATTERN, desc), violationType, url, paths)
        } else null
    }
}
