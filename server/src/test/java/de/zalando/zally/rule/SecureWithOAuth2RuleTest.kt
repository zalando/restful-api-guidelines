package de.zalando.zally.rule

import de.zalando.zally.dto.Violation
import de.zalando.zally.dto.ViolationType
import io.swagger.models.Scheme
import io.swagger.models.Swagger
import io.swagger.models.auth.ApiKeyAuthDefinition
import io.swagger.models.auth.BasicAuthDefinition
import io.swagger.models.auth.OAuth2Definition
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SecureWithOAuth2RuleTest {

    val expectedOauthViolation = Violation(
        SecureWithOAuth2Rule(),
        "Secure Endpoints with OAuth 2.0",
        "No OAuth2 security definitions found",
        ViolationType.MUST,
        SecureWithOAuth2Rule().url,
        emptyList())

    val expectedHttpsViolation = Violation(
            SecureWithOAuth2Rule(),
            "Secure Endpoints with OAuth 2.0",
            "OAuth2 should be only used together with https",
            ViolationType.MUST,
            SecureWithOAuth2Rule().url,
            emptyList())

    @Test
    fun emptySwagger() {
        assertThat(SecureWithOAuth2Rule().validate(Swagger())).isEqualTo(expectedOauthViolation)
    }

    @Test
    fun emptySecurityDefs() {
        val swagger = Swagger().apply {
            securityDefinitions = emptyMap()
        }
        assertThat(SecureWithOAuth2Rule().validate(swagger)).isEqualTo(expectedOauthViolation)
    }

    @Test
    fun noOAuthSecurityDef() {
        val swagger = Swagger().apply {
            securityDefinitions = mapOf(
                "Basic" to BasicAuthDefinition(),
                "ApiKey" to ApiKeyAuthDefinition()
            )
        }
        assertThat(SecureWithOAuth2Rule().validate(swagger)).isEqualTo(expectedOauthViolation)
    }

    @Test
    fun usesHttpScheme() {
        val swagger = Swagger().apply {
            schemes = listOf(Scheme.HTTP, Scheme.HTTPS)
            securityDefinitions = mapOf(
                    "Oauth2" to OAuth2Definition()
            )
        }
        assertThat(SecureWithOAuth2Rule().validate(swagger)).isEqualTo(expectedHttpsViolation)
    }

    @Test
    fun positiveCase() {
        val swagger = Swagger().apply {
            schemes = listOf(Scheme.HTTPS)
            securityDefinitions = mapOf(
                "Basic" to BasicAuthDefinition(),
                "Oauth2" to OAuth2Definition()
            )
        }
        assertThat(SecureWithOAuth2Rule().validate(swagger)).isNull()
    }
}
