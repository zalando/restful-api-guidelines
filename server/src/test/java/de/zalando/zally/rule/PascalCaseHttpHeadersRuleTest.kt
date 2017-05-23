package de.zalando.zally.rule

import de.zalando.zally.getFixture
import de.zalando.zally.swaggerWithHeaderParams
import de.zalando.zally.testConfig
import io.swagger.models.Swagger
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PascalCaseHttpHeadersRuleTest {

    @Test
    fun simplePoisitiveCase() {
        val swagger = swaggerWithHeaderParams("Right-Name")
        assertThat(PascalCaseHttpHeadersRule(testConfig).validate(swagger)).isNull()
    }

    @Test
    fun simpleNegativeCase() {
        val swagger = swaggerWithHeaderParams("kebap-case-name")
        val result = PascalCaseHttpHeadersRule(testConfig).validate(swagger)!!
        assertThat(result.paths).hasSameElementsAs(listOf("parameters kebap-case-name"))
    }

    @Test
    fun mustAcceptETag() {
        val swagger = swaggerWithHeaderParams("ETag")
        assertThat(PascalCaseHttpHeadersRule(testConfig).validate(swagger)).isNull()
    }

    @Test
    fun mustAccepZalandoHeaders() {
        val swagger = swaggerWithHeaderParams("X-Flow-ID", "X-UID", "X-Tenant-ID", "X-Sales-Channel", "X-Frontend-Type",
            "X-Device-Type", "X-Device-OS", "X-App-Domain")
        assertThat(PascalCaseHttpHeadersRule(testConfig).validate(swagger)).isNull()
    }

    @Test
    fun mustAcceptDigits() {
        val swagger = swaggerWithHeaderParams("X-P1n-Id")
        assertThat(PascalCaseHttpHeadersRule(testConfig).validate(swagger)).isNull()
    }

    @Test
    fun emptySwaggerShouldPass() {
        assertThat(PascalCaseHttpHeadersRule(testConfig).validate(Swagger())).isNull()
    }

    @Test
    fun positiveCaseSpp() {
        val swagger = getFixture("api_spp.json")
        assertThat(PascalCaseHttpHeadersRule(testConfig).validate(swagger)).isNull()
    }

    @Test
    fun positiveCaseTinbox() {
        val swagger = getFixture("api_tinbox.yaml")
        assertThat(PascalCaseHttpHeadersRule(testConfig).validate(swagger)).isNull()
    }
}

