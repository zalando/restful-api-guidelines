package de.zalando.zally.rules

import de.zalando.zally.getFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test


class SnakeCaseForQueryParamsRuleTest {

    private val validSwagger = getFixture("snakeCaseForQueryParamsValid.json")
    private val invalidSwaggerWithLocalParam = getFixture("snakeCaseForQueryParamsInvalidLocalParam.json")
    private val invalidSwaggerWIthInternalRef = getFixture("snakeCaseForQueryParamsInvalidInternalRef.json")
    private val invalidSwaggerWithExternalRef = getFixture("snakeCaseForQueryParamsInvalidExternalRef.json")

    @Test
    fun shouldFindNoViolations() {
        assertThat(SnakeCaseForQueryParamsRule().validate(validSwagger)).isNull()
    }

    @Test
    fun shouldFindViolationsInLocalRef() {
        val result = SnakeCaseForQueryParamsRule().validate(invalidSwaggerWithLocalParam)!!
        assertThat(result.paths).hasSameElementsAs(listOf(""))
    }

    @Test
    fun shouldFindViolationsInInternalRef() {
        val result = SnakeCaseForQueryParamsRule().validate(invalidSwaggerWIthInternalRef)!!
        assertThat(result.paths).hasSameElementsAs(listOf(""))
    }

    @Test
    fun shouldFindViolationsInExternalRef() {
        val result = SnakeCaseForQueryParamsRule().validate(invalidSwaggerWithExternalRef)!!
        assertThat(result.paths).hasSameElementsAs(listOf(""))
    }
}
