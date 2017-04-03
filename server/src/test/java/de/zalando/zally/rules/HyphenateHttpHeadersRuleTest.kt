package de.zalando.zally.rules

import com.typesafe.config.ConfigFactory
import de.zalando.zally.getFixture
import de.zalando.zally.swaggerWithHeaderParams
import io.swagger.models.Swagger
import io.swagger.models.parameters.Parameter
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.HashMap

class HyphenateHttpHeadersRuleTest {
    val ruleConfig = ConfigFactory.load("rules-config-test.conf")

    @Test
    fun simplePositiveCase() {
        val swagger = swaggerWithHeaderParams("Right-Name")
        assertThat(HyphenateHttpHeadersRule(ruleConfig).validate(swagger)).isNull()
    }

    @Test
    fun simpleNegativeCase() {
        val swagger = swaggerWithHeaderParams("CamelCaseName")
        val result = HyphenateHttpHeadersRule(ruleConfig).validate(swagger)!!
        assertThat(result.paths).hasSameElementsAs(listOf("parameters CamelCaseName"))
    }

    @Test
    fun mustAcceptETag() {
        val swagger = swaggerWithHeaderParams("ETag")
        assertThat(HyphenateHttpHeadersRule(ruleConfig).validate(swagger)).isNull()
    }

    @Test
    fun emptySwaggerShouldPass() {
        val swagger = Swagger()
        swagger.parameters = HashMap<String, Parameter>()
        assertThat(HyphenateHttpHeadersRule(ruleConfig).validate(swagger)).isNull()
    }

    @Test
    fun positiveCaseSpp() {
        val swagger = getFixture("api_spp.json")
        assertThat(HyphenateHttpHeadersRule(ruleConfig).validate(swagger)).isNull()
    }

    @Test
    fun positiveCaseTinbox() {
        val swagger = getFixture("api_tinbox.yaml")
        assertThat(HyphenateHttpHeadersRule(ruleConfig).validate(swagger)).isNull()
    }
}
