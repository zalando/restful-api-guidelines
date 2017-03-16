package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
class LimitNumberOfResourcesRule : AbstractRule() {
    override val title = "Limit number of Resources"
    override val url = "http://zalando.github.io/restful-api-guidelines/resources/Resources.html" +
            "#should-limit-number-of-resources"
    override val violationType = ViolationType.SHOULD
    private val PATHS_COUNT_LIMIT = 8

    override fun validate(swagger: Swagger): Violation? {
        val paths = swagger.paths.orEmpty()
        val pathsCount = paths.size
        return if (pathsCount > PATHS_COUNT_LIMIT) {
            Violation(this, title, "Number of paths $pathsCount is greater than $PATHS_COUNT_LIMIT",
                    violationType, url, paths.keys.toList())
        } else null
    }
}
