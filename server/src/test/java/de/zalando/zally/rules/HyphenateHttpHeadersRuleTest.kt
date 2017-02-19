package de.zalando.zally.rules

import de.zalando.zally.getFixture
import de.zalando.zally.swaggerWithParams
import io.swagger.models.Swagger
import io.swagger.models.parameters.Parameter
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class HyphenateHttpHeadersRuleTest {

    @Test
    fun simplePositiveCase() {
        val swagger = swaggerWithParams("Right-Name")
        assertThat(HyphenateHttpHeadersRule().validate(swagger)).isNull()
    }

    @Test
    fun simpleNegativeCase() {
        val swagger = swaggerWithParams("CamelCaseName")
        val result = HyphenateHttpHeadersRule().validate(swagger)!!
        assertThat(result.paths).hasSameElementsAs(listOf("parameter CamelCaseName"))
    }

    @Test
    fun mustAcceptETag() {
        val swagger = swaggerWithParams("ETag")
        assertThat(HyphenateHttpHeadersRule().validate(swagger)).isNull()
    }

    @Test
    fun emptySwaggerShouldPass() {
        val swagger = Swagger()
        swagger.parameters = HashMap<String, Parameter>()
        assertThat(HyphenateHttpHeadersRule().validate(swagger)).isNull()
    }

    @Test
    fun positiveCaseSpp() {
        val swagger = getFixture("api_spp.json")
        assertThat(HyphenateHttpHeadersRule().validate(swagger)).isNull()
    }

    @Test
    fun positiveCaseTinbox() {
        val swagger = getFixture("api_tinbox.yaml")
        assertThat(HyphenateHttpHeadersRule().validate(swagger)).isNull()
    }
}
