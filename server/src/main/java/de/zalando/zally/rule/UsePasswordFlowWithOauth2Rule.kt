package de.zalando.zally.rule

import de.zalando.zally.dto.Violation
import de.zalando.zally.dto.ViolationType
import io.swagger.models.Swagger
import io.swagger.models.auth.OAuth2Definition

class UsePasswordFlowWithOauth2Rule : AbstractRule() {
    override val title = "Set Flow to Password When Using OAuth 2.0"
    override val url = "https://zalando.github.io/restful-api-guidelines/security/Security.html" +
            "#must-secure-endpoints-with-oauth-20"
    override val violationType = ViolationType.MUST
    override val code = "M017"

    override fun validate(swagger: Swagger): Violation? {
        val definitionsWithoutPasswordFlow = swagger
                .securityDefinitions
                .orEmpty()
                .values
                .filter { it.type?.toLowerCase() == "oauth2" }
                .filter { (it as OAuth2Definition).flow != "password" }

        return if (definitionsWithoutPasswordFlow.any())
            Violation(this, title, "OAuth2 security definitions should use password flow", violationType, url, emptyList())
        else null
    }
}
