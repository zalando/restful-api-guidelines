package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.PatternUtil.isPathVariable
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
open class NestedPathsCouldBeRootPathsRule : AbstractRule() {
    private val TITLE = "Consider Using (Non-) Nested URLs"
    private val DESCRIPTION = "Nested paths / URLs could be top-level resource"
    private val RULE_LINK = "http://zalando.github.io/restful-api-guidelines/resources/Resources.html" +
            "#could-consider-using-non-nested-urls"

    override fun validate(swagger: Swagger): Violation? {
        val paths = swagger.paths.orEmpty().keys.filter {
            val pathSegments = it.split("/".toRegex())
            // we are only interested in paths that have sub-resource followed by a param: /path1/{param1}/path2/{param2}
            pathSegments.size > 4 && isPathVariable(pathSegments.last())
        }
        return if (paths.isNotEmpty()) Violation(this, TITLE, DESCRIPTION, ViolationType.COULD, RULE_LINK, paths) else null
    }
}
