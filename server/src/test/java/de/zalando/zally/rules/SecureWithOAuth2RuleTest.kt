package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Swagger
import io.swagger.models.auth.ApiKeyAuthDefinition
import io.swagger.models.auth.BasicAuthDefinition
import io.swagger.models.auth.OAuth2Definition
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test


class SecureWithOAuth2RuleTest {
    val expectedViolation = Violation(
            "Secure Endpoints with OAuth 2.0",
            "No OAuth2 security definitions found",
            ViolationType.MUST,
            SecureWithOAuth2Rule().LINK,
            emptyList())

    @Test
    fun emptySwagger() {
        assertThat(SecureWithOAuth2Rule().validate(Swagger())).isEqualTo(expectedViolation)
    }

    @Test
    fun emptySecurityDefs() {
        val swagger = Swagger().apply {
            securityDefinitions = emptyMap()
        }
        assertThat(SecureWithOAuth2Rule().validate(swagger)).isEqualTo(expectedViolation)
    }

    @Test
    fun noOAuthSecurityDef() {
        val swagger = Swagger().apply {
            securityDefinitions = mapOf(
                    "Basic" to BasicAuthDefinition(),
                    "ApiKey" to ApiKeyAuthDefinition()
            )
        }
        assertThat(SecureWithOAuth2Rule().validate(swagger)).isEqualTo(expectedViolation)
    }

    @Test
    fun positiveCase() {
        val swagger = Swagger().apply {
            securityDefinitions = mapOf(
                    "Basic" to BasicAuthDefinition(),
                    "Oauth2" to OAuth2Definition()
            )
        }
        assertThat(SecureWithOAuth2Rule().validate(swagger)).isNull()
    }
}