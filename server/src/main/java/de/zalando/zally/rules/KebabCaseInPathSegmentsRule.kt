package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.PatternUtil
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
class KebabCaseInPathSegmentsRule : AbstractRule() {

    override val title = "Lowercase words with hyphens"
    override val url = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html" +
            "#must-use-lowercase-separate-words-with-hyphens-for-path-segments"
    override val violationType = ViolationType.MUST
    private val description = "Use lowercase separate words with hyphens for path segments"

    override fun validate(swagger: Swagger): Violation? {
        val paths = swagger.paths.orEmpty().keys.filterNot {
            val pathSegments = it.split("/").filter { it.isNotEmpty() }
            pathSegments.filter { !PatternUtil.isPathVariable(it) && !PatternUtil.isLowerCaseAndHyphens(it) }.isEmpty()
        }
        return if (paths.isNotEmpty()) Violation(this, title, description, violationType, url, paths) else null
    }
}
