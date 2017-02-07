package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Swagger
import io.swagger.models.parameters.HeaderParameter
import io.swagger.models.parameters.Parameter
import io.swagger.parser.SwaggerParser
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.HashMap

class HyphenateHttpHeadersRuleTest {
    private fun createSwaggerWithParam(name: String): Swagger {
        val swagger = Swagger()
        val parameters = HashMap<String, Parameter>()
        val parameter = HeaderParameter()
        parameter.name = name
        parameters.put(parameter.name, parameter)
        swagger.parameters = parameters
        return swagger
    }

    @Test
    fun simplePoisitiveCase() {
        val swagger = createSwaggerWithParam("Right-Name")
        assertThat(HyphenateHttpHeadersRule().validate(swagger)).isEmpty()
    }

    @Test
    fun simpleNegativeCase() {
        val swagger = createSwaggerWithParam("CamelCaseName")
        val rule = HyphenateHttpHeadersRule()
        assertThat(rule.validate(swagger)).hasSameElementsAs(listOf(Violation(rule.RULE_NAME,
                "Header name 'CamelCaseName' is not hyphenated", ViolationType.MUST, rule.RULE_URL)))
    }

    @Test
    fun mustAcceptETag() {
        val swagger = createSwaggerWithParam("ETag")
        assertThat(HyphenateHttpHeadersRule().validate(swagger)).isEmpty()
    }

    @Test
    fun emptySwaggerShouldPass() {
        val swagger = Swagger()
        swagger.parameters = HashMap<String, Parameter>()
        assertThat(HyphenateHttpHeadersRule().validate(swagger)).isEmpty()
    }

    @Test
    fun positiveCaseSpp() {
        val swagger = SwaggerParser().read("api_spp.json")
        assertThat(HyphenateHttpHeadersRule().validate(swagger)).isEmpty()
    }

    @Test
    fun positiveCaseTinbox() {
        val swagger = SwaggerParser().read("api_tinbox.yaml")
        assertThat(HyphenateHttpHeadersRule().validate(swagger)).isEmpty()
    }
}
