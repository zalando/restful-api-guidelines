package de.zalando.zally.rules

import de.zalando.zally.getFixture
import de.zalando.zally.swaggerWithHeaderParams
import io.swagger.models.Swagger
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PascalCaseHttpHeadersRuleTest {

    @Test
    fun simplePoisitiveCase() {
        val swagger = swaggerWithHeaderParams("Right-Name")
        assertThat(PascalCaseHttpHeadersRule().validate(swagger)).isNull()
    }

    @Test
    fun simpleNegativeCase() {
        val swagger = swaggerWithHeaderParams("kebap-case-name")
        val result = PascalCaseHttpHeadersRule().validate(swagger)!!
        assertThat(result.paths).hasSameElementsAs(listOf("parameters kebap-case-name"))
    }

    @Test
    fun mustAcceptETag() {
        val swagger = swaggerWithHeaderParams("ETag")
        assertThat(PascalCaseHttpHeadersRule().validate(swagger)).isNull()
    }

    @Test
    fun mustAccepZalandoHeaders() {
        val swagger = swaggerWithHeaderParams("X-Flow-ID", "X-UID", "X-Tenant-ID", "X-Sales-Channel", "X-Frontend-Type",
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

