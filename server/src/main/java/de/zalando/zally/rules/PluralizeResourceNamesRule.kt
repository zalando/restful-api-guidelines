package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.PatternUtil
import de.zalando.zally.utils.WordUtil.isPlural
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
open class PluralizeResourceNamesRule : Rule {
    val RULE_NAME = "Pluralize Resource Names"
    val DESC_PATTERN = "Resources %s are singular (but we are not sure)"
    val RULE_URL = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html" +
            "#must-pluralize-resource-names"

    override fun validate(swagger: Swagger): Violation? {
        val res = swagger.paths.keys.flatMap { path ->
            path.split("/".toRegex()).filter { s -> !s.isEmpty() && !PatternUtil.isPathVariable(s) && !isPlural(s) }
                    .map { it to path }
        }
        return if (res.isNotEmpty()) {
            val desc = res.toSet().map { "'${it.first}'" }.joinToString(", ")
            val paths = res.map { it.second }
            Violation(RULE_NAME, String.format(DESC_PATTERN, desc), ViolationType.SHOULD, RULE_URL, paths)
        } else null
    }
}
