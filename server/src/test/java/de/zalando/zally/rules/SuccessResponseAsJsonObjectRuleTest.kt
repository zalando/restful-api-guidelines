package de.zalando.zally.rules

import de.zalando.zally.getFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SuccessResponseAsJsonObjectRuleTest {

    private val validSwagger = getFixture("successResponseAsJsonObjectValid.json")
    private val invalidSwagger = getFixture("successResponseAsJsonObjectInvalid.json")
    private val npeSwagger = getFixture("sample_swagger_api.yaml")

    @Test
    fun positiveCase() {
        assertThat(SuccessResponseAsJsonObjectRule().validate(validSwagger)).isNull()
    }

    @Test
    fun negativeCase() {
        val result = SuccessResponseAsJsonObjectRule().validate(invalidSwagger)!!
        assertThat(result.paths).hasSameElementsAs(listOf("/pets GET 200", "/pets POST 200"))
    }

    @Test
    fun positiveCaseSpp() {
        val swagger = getFixture("api_spp.json")
        assertThat(SuccessResponseAsJsonObjectRule().validate(swagger)).isNull()
    }

    @Test
    fun npeBug() {
        assertThat(SuccessResponseAsJsonObjectRule().validate(npeSwagger)).isNotNull()
    }
}
