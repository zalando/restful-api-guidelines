package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.getFixture
import io.swagger.models.Swagger
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class NoProtocolInHostRuleTest {

    val expectedViolation = NoProtocolInHostRule().let {
        Violation(it, it.title, it.desc, it.violationType, it.url, emptyList())
    }

    @Test
    fun emptySwagger() {
        val swagger = Swagger()
        assertThat(NoProtocolInHostRule().validate(swagger)).isNull()
    }

    @Test
    fun positiveCase() {
        val swagger = Swagger().apply { host = "google.com" }
        assertThat(NoProtocolInHostRule().validate(swagger)).isNull()
    }

    @Test
    fun negativeCaseHttp() {
        val swagger = Swagger().apply { host = "http://google.com" }
        assertThat(NoProtocolInHostRule().validate(swagger)).isEqualTo(expectedViolation)
    }

    @Test
    fun negativeCaseHttps() {
        val swagger = Swagger().apply { host = "https://google.com" }
        assertThat(NoProtocolInHostRule().validate(swagger)).isEqualTo(expectedViolation)
    }

    @Test
    fun positiveCaseSpp() {
        val swagger = getFixture("api_spp.json")
        assertThat(AvoidLinkHeadersRule().validate(swagger)).isNull()
    }

    @Test
    fun positiveCaseSpa() {
        val swagger = getFixture("api_spa.yaml")
        assertThat(AvoidLinkHeadersRule().validate(swagger)).isNull()
    }
}
