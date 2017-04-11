package de.zalando.zally.rules

import de.zalando.zally.getFixture
import de.zalando.zally.swaggerWithHeaderParams
import de.zalando.zally.testConfig
import io.swagger.models.Swagger
import io.swagger.models.parameters.Parameter
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.HashMap

class HyphenateHttpHeadersRuleTest {

    @Test
    fun simplePositiveCase() {
        val swagger = swaggerWithHeaderParams("Right-Name")
        assertThat(HyphenateHttpHeadersRule(testConfig).validate(swagger)).isNull()
    }

    @Test
    fun simpleNegativeCase() {
        val swagger = swaggerWithHeaderParams("CamelCaseName")
        val result = HyphenateHttpHeadersRule(testConfig).validate(swagger)!!
        assertThat(result.paths).hasSameElementsAs(listOf("parameters CamelCaseName"))
    }

    @Test
    fun mustAcceptETag() {
        val swagger = swaggerWithHeaderParams("ETag")
        assertThat(HyphenateHttpHeadersRule(testConfig).validate(swagger)).isNull()
    }

    @Test
    fun emptySwaggerShouldPass() {
        val swagger = Swagger()
        swagger.parameters = HashMap<String, Parameter>()
        assertThat(HyphenateHttpHeadersRule(testConfig).validate(swagger)).isNull()
    }

    @Test
    fun positiveCaseSpp() {
        val swagger = getFixture("api_spp.json")
        assertThat(HyphenateHttpHeadersRule(testConfig).validate(swagger)).isNull()
    }

    @Test
    fun positiveCaseTinbox() {
        val swagger = getFixture("api_tinbox.yaml")
        assertThat(HyphenateHttpHeadersRule(testConfig).validate(swagger)).isNull()
    }
}
