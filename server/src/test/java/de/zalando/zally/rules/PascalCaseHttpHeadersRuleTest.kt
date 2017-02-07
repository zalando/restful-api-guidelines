package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Swagger
import io.swagger.models.parameters.HeaderParameter
import io.swagger.models.parameters.Parameter
import io.swagger.parser.SwaggerParser
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class PascalCaseHttpHeadersRuleTest {
    private fun createSwaggerWithParams(vararg names: String) =
        Swagger().apply {
            parameters = names.map { header ->
                Pair(header, HeaderParameter().apply { name = header })
            }.toMap()
        }

    @Test
    fun simplePoisitiveCase() {
        val swagger = createSwaggerWithParams("Right-Name")
        assertThat(PascalCaseHttpHeadersRule().validate(swagger)).isEmpty()
    }

    @Test
    fun simpleNegativeCase() {
        val badHeader = "kebap-case-name"
        val swagger = createSwaggerWithParams(badHeader)
        val rule = PascalCaseHttpHeadersRule()
        val result = rule.validate(swagger)
        assertThat(result).hasSameElementsAs(listOf(Violation(rule.RULE_NAME,
                "Header name 'kebap-case-name' is not Hyphenated-Pascal-Case", ViolationType.SHOULD, rule.RULE_URL)))
    }

    @Test
    fun mustAcceptETag() {
        val swagger = createSwaggerWithParams("ETag")
        assertThat(PascalCaseHttpHeadersRule().validate(swagger)).isEmpty()
    }

    @Test
    fun mustAccepZalandoHeaders() {
        val swagger = createSwaggerWithParams("X-Flow-ID", "X-UID", "X-Tenant-ID", "X-Sales-Channel", "X-Frontend-Type",
                "X-Device-Type", "X-Device-OS", "X-App-Domain")
        assertThat(PascalCaseHttpHeadersRule().validate(swagger)).isEmpty()
    }

    @Test
    fun emptySwaggerShouldPass() {
        val swagger = Swagger()
        swagger.parameters = HashMap<String, Parameter>()
        assertThat(PascalCaseHttpHeadersRule().validate(swagger)).isEmpty()
    }

    @Test
    fun positiveCaseSpp() {
        val swagger = SwaggerParser().read("api_spp.json")
        assertThat(PascalCaseHttpHeadersRule().validate(swagger)).isEmpty()
    }

    @Test
    fun positiveCaseTinbox() {
        val swagger = SwaggerParser().read("api_tinbox.yaml")
        assertThat(PascalCaseHttpHeadersRule().validate(swagger)).isEmpty()
    }
}

