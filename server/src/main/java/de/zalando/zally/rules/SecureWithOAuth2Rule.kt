package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
open class SecureWithOAuth2Rule : AbstractRule() {
    val TITLE = "Secure Endpoints with OAuth 2.0"
    val LINK = "https://zalando.github.io/restful-api-guidelines/security/Security.html" +
            "#must-secure-endpoints-with-oauth-20"

    override fun validate(swagger: Swagger): Violation? {
        val hasOAuth = swagger.securityDefinitions.orEmpty().values.any { it.type?.toLowerCase() == "oauth2" }
        return if (!hasOAuth)
            Violation(this, TITLE, "No OAuth2 security definitions found", ViolationType.MUST, LINK, emptyList())
        else null
    }
}
