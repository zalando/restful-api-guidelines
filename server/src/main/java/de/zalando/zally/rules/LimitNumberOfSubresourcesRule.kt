package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.PatternUtil
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
class LimitNumberOfSubresourcesRule : AbstractRule() {
    override val title = "Limit number of Sub-resources level"
    override val url = "http://zalando.github.io/restful-api-guidelines/resources/Resources.html" +
            "#should-limit-number-of-subresource-levels"
    override val violationType = ViolationType.SHOULD
    override val code = "S003"
    private val DESC = "Number of sub-resources should not exceed 3"
    private val SUBRESOURCES_LIMIT = 3

    override fun validate(swagger: Swagger): Violation? {
        val paths = swagger.paths.orEmpty().keys.filter { path ->
            path.split("/").filter { it.isNotEmpty() && !PatternUtil.isPathVariable(it) }.size - 1 > SUBRESOURCES_LIMIT
        }
        return if (paths.isNotEmpty()) Violation(this, title, DESC, violationType, url, paths) else null
    }
}
