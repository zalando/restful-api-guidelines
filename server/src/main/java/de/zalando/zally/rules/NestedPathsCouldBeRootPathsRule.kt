package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.PatternUtil.isPathVariable
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
class NestedPathsCouldBeRootPathsRule : AbstractRule() {
    override val title = "Consider Using (Non-) Nested URLs"
    override val url = "http://zalando.github.io/restful-api-guidelines/resources/Resources.html" +
            "#could-consider-using-non-nested-urls"
    override val violationType = ViolationType.COULD
    private val DESCRIPTION = "Nested paths / URLs could be top-level resource"

    override fun validate(swagger: Swagger): Violation? {
        val paths = swagger.paths.orEmpty().keys.filter {
            val pathSegments = it.split("/".toRegex())
            // we are only interested in paths that have sub-resource followed by a param: /path1/{param1}/path2/{param2}
            pathSegments.size > 4 && isPathVariable(pathSegments.last())
        }
        return if (paths.isNotEmpty()) Violation(this, title, DESCRIPTION, violationType, url, paths) else null
    }
}
