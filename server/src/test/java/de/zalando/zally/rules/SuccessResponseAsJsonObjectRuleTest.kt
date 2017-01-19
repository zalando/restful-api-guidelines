package de.zalando.zally.rules

import io.swagger.parser.SwaggerParser
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SuccessResponseAsJsonObjectRuleTest {
    private val validSwagger = getFixture("fixtures/successResponseAsJsonObjectValid.json")
    private val invalidSwagger = getFixture("fixtures/successResponseAsJsonObjectInvalid.json")


    @Test
    fun positiveCase() {
        assertThat(SuccessResponseAsJsonObjectRule().validate(validSwagger)).isEmpty()
    }

    @Test
    fun negativeCase() {
        val results = SuccessResponseAsJsonObjectRule().validate(invalidSwagger).map { it.path.get() }
        assertThat(results).hasSameElementsAs(listOf("/pets/GET/200", "/pets/POST/200"))
    }

    @Test
    fun positiveCaseSpp() {
        val swagger = getFixture("api_spp.json")
        assertThat(SuccessResponseAsJsonObjectRule().validate(swagger)).isEmpty()
    }

    private fun getFixture(fixture: String) = SwaggerParser().read(fixture)
}
