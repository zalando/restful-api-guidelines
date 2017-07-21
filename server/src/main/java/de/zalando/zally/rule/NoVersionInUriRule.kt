package de.zalando.zally.rule

import de.zalando.zally.dto.Violation
import de.zalando.zally.dto.ViolationType
import de.zalando.zally.util.PatternUtil
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
class NoVersionInUriRule : AbstractRule() {
    override val title = "Do Not Use URI Versioning"
    override val url = "https://zalando.github.io/restful-api-guidelines/compatibility/Compatibility.html" +
        "#must-do-not-use-uri-versioning"
    override val violationType = ViolationType.MUST
    override val code = "M009"
    private val description = "basePath attribute contains version number"

    override fun validate(swagger: Swagger): Violation? {
        val hasVersion = swagger.basePath != null && PatternUtil.hasVersionInUrl(swagger.basePath)
        return if (hasVersion) Violation(this, title, description, violationType, url, emptyList()) else null
    }
}
