package de.zalando.zally.rule

import de.zalando.zally.getFixture
import io.swagger.models.Swagger
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class DefineOAuthScopesRuleTest {

    @Test
    fun emptyAPI() {
        assertThat(DefineOAuthScopesRule().validate(Swagger())).isNull()
    }

    @Test
    fun apiWithoutScope() {
        val swagger = getFixture("api_without_scopes_defined.yaml")
        assertThat(DefineOAuthScopesRule().validate(swagger)!!.paths).hasSize(4)
    }

    @Test
    fun apiWithDefinedScope() {
        val swagger = getFixture("api_with_defined_scope.yaml")
        assertThat(DefineOAuthScopesRule().validate(swagger)).isNull()
    }

    @Test
    fun apiWithUndefinedScope() {
        val swagger = getFixture("api_with_undefined_scope.yaml")
        assertThat(DefineOAuthScopesRule().validate(swagger)!!.paths).hasSize(2)
    }

    @Test
    fun apiWithDefinedAndUndefinedScope() {
        val swagger = getFixture("api_with_defined_and_undefined_scope.yaml")
        assertThat(DefineOAuthScopesRule().validate(swagger)!!.paths).hasSize(2)
    }

    @Test
    fun apiWithDefinedTopLevelScope() {
        val swagger = getFixture("api_with_toplevel_scope.yaml")
        assertThat(DefineOAuthScopesRule().validate(swagger)).isNull()
    }
}
