package de.zalando.zally.rules

import de.zalando.zally.getFixture
import de.zalando.zally.swaggerWithParams
import io.swagger.models.Swagger
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PascalCaseHttpHeadersRuleTest {

    @Test
    fun simplePoisitiveCase() {
        val swagger = swaggerWithParams("Right-Name")
        assertThat(PascalCaseHttpHeadersRule().validate(swagger)).isNull()
    }

    @Test
    fun simpleNegativeCase() {
        val badName = "kebap-case-name"
        val swagger = swaggerWithParams(badName)
        val result = PascalCaseHttpHeadersRule().validate(swagger)!!
        assertThat(result.description).contains(badName)
    }

    @Test
    fun mustAcceptETag() {
        val swagger = swaggerWithParams("ETag")
        assertThat(PascalCaseHttpHeadersRule().validate(swagger)).isNull()
    }

    @Test
    fun mustAccepZalandoHeaders() {
        val swagger = swaggerWithParams("X-Flow-ID", "X-UID", "X-Tenant-ID", "X-Sales-Channel", "X-Frontend-Type",
                "X-Device-Type", "X-Device-OS", "X-App-Domain")
        assertThat(PascalCaseHttpHeadersRule().validate(swagger)).isNull()
    }

    @Test
    fun emptySwaggerShouldPass() {
        assertThat(PascalCaseHttpHeadersRule().validate(Swagger())).isNull()
    }

    @Test
    fun positiveCaseSpp() {
        val swagger = getFixture("api_spp.json")
        assertThat(PascalCaseHttpHeadersRule().validate(swagger)).isNull()
    }

    @Test
    fun positiveCaseTinbox() {
        val swagger = getFixture("api_tinbox.yaml")
        assertThat(PascalCaseHttpHeadersRule().validate(swagger)).isNull()
    }
}

