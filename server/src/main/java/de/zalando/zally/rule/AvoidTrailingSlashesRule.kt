package de.zalando.zally.rule

import de.zalando.zally.dto.ViolationType
import de.zalando.zally.util.PatternUtil
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
class AvoidTrailingSlashesRule : AbstractRule() {
    override val title = "Avoid Trailing Slashes"
    override val url = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html"
    override val violationType = ViolationType.MUST
    override val code = "M002"
    private val DESCRIPTION = "Rule avoid trailing slashes is not followed"

    override fun validate(swagger: Swagger): Violation? {
        val paths = swagger.paths.orEmpty().keys.filter { it != null && PatternUtil.hasTrailingSlash(it) }
        return if (!paths.isEmpty()) Violation(this, title, DESCRIPTION, violationType, url, paths) else null
    }
}
