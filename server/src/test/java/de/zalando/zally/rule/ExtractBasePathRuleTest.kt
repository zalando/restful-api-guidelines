package de.zalando.zally.rule

import de.zalando.zally.dto.Violation
import de.zalando.zally.dto.ViolationType
import de.zalando.zally.getFixture
import de.zalando.zally.swaggerWithPaths
import io.swagger.models.Swagger
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ExtractBasePathRuleTest {
    val DESC_PATTERN = "All paths start with prefix '%s'. This prefix could be part of base path."

    @Test
    fun validateEmptyPath() {
        assertThat(ExtractBasePathRule().validate(Swagger())).isNull()
    }

    @Test
    fun simplePositiveCase() {
        val swagger = swaggerWithPaths("/orders/{order_id}", "/orders/{updates}", "/merchants")
        assertThat(ExtractBasePathRule().validate(swagger)).isNull()
    }

    @Test
    fun singlePathShouldPass() {
        val swagger = swaggerWithPaths("/orders/{order_id}")
        assertThat(ExtractBasePathRule().validate(swagger)).isNull()
    }

    @Test
    fun simpleNegativeCase() {
        val swagger = swaggerWithPaths(
            "/shipment/{shipment_id}",
            "/shipment/{shipment_id}/status",
            "/shipment/{shipment_id}/details"
        )
        val rule = ExtractBasePathRule()
        val expected = Violation(ExtractBasePathRule(), rule.title, DESC_PATTERN.format("/shipment"),
            ViolationType.HINT, rule.url, emptyList())
        assertThat(rule.validate(swagger)).isEqualTo(expected)
    }

    @Test
    fun multipleResourceNegativeCase() {
        val swagger = swaggerWithPaths(
            "/queue/models/configs/{config-id}",
            "/queue/models/",
            "/queue/models/{model-id}",
            "/queue/models/summaries"
        )
        val rule = ExtractBasePathRule()
        val expected = Violation(ExtractBasePathRule(), rule.title, DESC_PATTERN.format("/queue/models"),
            ViolationType.HINT, rule.url, emptyList())
        assertThat(rule.validate(swagger)).isEqualTo(expected)
    }

    @Test
    fun shouldMatchWholeSubresource() {
        val swagger = swaggerWithPaths(
            "/api/{api_id}/deployments",
            "/api/{api_id}/",
            "/applications/{app_id}",
            "/applications/"
        )
        assertThat(ExtractBasePathRule().validate(swagger)).isNull()
    }

    @Test
    fun positiveCaseSpp() {
        val swagger = getFixture("api_spp.json")
        assertThat(ExtractBasePathRule().validate(swagger)).isNull()
    }

    @Test
    fun positiveCaseTinbox() {
        val swagger = getFixture("api_tinbox.yaml")
        assertThat(ExtractBasePathRule().validate(swagger)).isNull()
    }
}
