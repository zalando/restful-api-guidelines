package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
open class LimitNumberOfResourcesRule : AbstractRule() {
    val TITLE = "Limit number of Resources"
    val RULE_LINK = "http://zalando.github.io/restful-api-guidelines/resources/Resources.html" +
            "#should-limit-number-of-resources"
    val PATHS_COUNT_LIMIT = 8

    override fun validate(swagger: Swagger): Violation? {
        val paths = swagger.paths.orEmpty()
        val pathsCount = paths.size
        return if (pathsCount > PATHS_COUNT_LIMIT) {
            Violation(this, TITLE, "Number of paths $pathsCount is greater than $PATHS_COUNT_LIMIT",
                    ViolationType.SHOULD, RULE_LINK, paths.keys.toList())
        } else null
    }
}
