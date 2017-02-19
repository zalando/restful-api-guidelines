package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.PatternUtil
import io.swagger.models.Swagger
import org.springframework.stereotype.Component


@Component
open class NoVersionInUriRule : Rule {
    val TITLE = "Do Not Use URI Versioning"
    val DESC = "basePath attribute contains version number"
    val LINK = "https://zalando.github.io/restful-api-guidelines/compatibility/Compatibility.html" +
            "#must-do-not-use-uri-versioning"

    override fun validate(swagger: Swagger): Violation? {
        val hasVersion = swagger.basePath != null && PatternUtil.hasVersionInUrl(swagger.basePath)
        return if (hasVersion) Violation(TITLE, DESC, ViolationType.MUST, LINK, emptyList()) else null
    }
}
