package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType.MUST
import de.zalando.zally.utils.PatternUtil
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
open class AvoidTrailingSlashesRule : Rule {
    val TITLE = "Avoid Trailing Slashes"
    val DESCRIPTION = "Rule avoid trailing slashes is not followed"
    val RULE_LINK = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html"

    override fun validate(swagger: Swagger): Violation? {
        val paths = swagger.paths.orEmpty().keys.filter { it != null && PatternUtil.hasTrailingSlash(it) }
        return if (!paths.isEmpty()) Violation(TITLE, DESCRIPTION, MUST, RULE_LINK, paths) else null
    }
}
