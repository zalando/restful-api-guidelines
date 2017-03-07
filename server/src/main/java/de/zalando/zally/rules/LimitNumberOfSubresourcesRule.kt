package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.PatternUtil
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
open class LimitNumberOfSubresourcesRule : AbstractRule() {
    val TITLE = "Limit number of Sub-resources level"
    val DESC = "Number of sub-resources should not exceed 3"
    val RULE_LINK = "http://zalando.github.io/restful-api-guidelines/resources/Resources.html" +
            "#should-limit-number-of-subresource-levels"
    val SUBRESOURCES_LIMIT = 3

    override fun validate(swagger: Swagger): Violation? {
        val paths = swagger.paths.orEmpty().keys.filter { path ->
            path.split("/").filter { it.isNotEmpty() && !PatternUtil.isPathVariable(it) }.size - 1 > SUBRESOURCES_LIMIT
        }
        return if (paths.isNotEmpty()) Violation(this, TITLE, DESC, ViolationType.SHOULD, RULE_LINK, paths) else null
    }
}
