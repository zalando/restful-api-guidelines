package de.zalando.zally.rule

import de.zalando.zally.dto.Violation
import de.zalando.zally.dto.ViolationType
import io.swagger.models.Swagger
import io.swagger.models.auth.ApiKeyAuthDefinition
import io.swagger.models.auth.BasicAuthDefinition
import io.swagger.models.auth.OAuth2Definition
import org.assertj.core.api.Assertions
import org.junit.Test

class UsePasswordFlowWithOauth2RuleTest {

    val expectedViolation = Violation(
            UsePasswordFlowWithOauth2Rule(),
            "Set Flow to Password When Using OAuth 2.0",
            "OAuth2 security definitions should use password flow",
            ViolationType.MUST,
            UsePasswordFlowWithOauth2Rule().url,
            emptyList())

    @Test
    fun shouldReturnNoViolationsWhenNoOauth2Found() {
        val swagger = Swagger().apply {
            securityDefinitions = mapOf(
                    "Basic" to BasicAuthDefinition(),
                    "ApiKey" to ApiKeyAuthDefinition()
            )
        }
        Assertions.assertThat(UsePasswordFlowWithOauth2Rule().validate(swagger)).isNull()
    }

    @Test
    fun shouldReturnNoViolationsWhenOauth2DefinitionsHasProperFlow() {
        val swagger = Swagger().apply {
            securityDefinitions = mapOf(
                    "Basic" to BasicAuthDefinition(),
                    "Oauth2" to OAuth2Definition().apply {
                        flow = "password"
                    }
            )
        }
        Assertions.assertThat(UsePasswordFlowWithOauth2Rule().validate(swagger)).isNull()
    }

    @Test
    fun shouldReturnViolationsWhenOauth2DefinitionsHasWrongFlow() {
        val swagger = Swagger().apply {
            securityDefinitions = mapOf(
                    "Basic" to BasicAuthDefinition(),
                    "Oauth2" to OAuth2Definition().apply {
                        flow = "implicit"
                    }
            )
        }
        Assertions.assertThat(UsePasswordFlowWithOauth2Rule().validate(swagger)).isEqualTo(expectedViolation)
    }

    @Test
    fun shouldReturnViolationsWhenOauth2DefinitionsHasNoFlow() {
        val swagger = Swagger().apply {
            securityDefinitions = mapOf(
                    "Basic" to BasicAuthDefinition(),
                    "Oauth2" to OAuth2Definition()
            )
        }
        Assertions.assertThat(UsePasswordFlowWithOauth2Rule().validate(swagger)).isEqualTo(expectedViolation)
    }

    @Test
    fun shouldReturnViolationsWhenOneOfOauth2DefinitionsIsWrong() {
        val swagger = Swagger().apply {
            securityDefinitions = mapOf(
                    "Oauth2A" to OAuth2Definition(),
                    "Oauth2B" to OAuth2Definition().apply {
                        flow = "password"
                    }
            )
        }
        Assertions.assertThat(UsePasswordFlowWithOauth2Rule().validate(swagger)).isEqualTo(expectedViolation)
    }
}
