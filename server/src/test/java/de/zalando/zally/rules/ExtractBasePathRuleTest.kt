package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.getFixture
import io.swagger.models.Path
import io.swagger.models.Swagger
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ExtractBasePathRuleTest {
    private fun createSwaggerWithPaths(vararg testPaths: String) =
            Swagger().apply {
                paths = testPaths.map { it to Path() }.toMap()
            }

    @Test
    fun validateEmptyPath() {
        assertThat(ExtractBasePathRule().validate(Swagger())).isNull()
    }

    @Test
    fun simplePositiveCase() {
        val swagger = createSwaggerWithPaths("/orders/{order_id}", "/orders/{updates}", "/merchants")
        assertThat(ExtractBasePathRule().validate(swagger)).isNull()
    }

    @Test
    fun simpleNegativeCase() {
        val swagger = createSwaggerWithPaths(
                "/shipment/{shipment_id}",
                "/shipment/{shipment_id}/status",
                "/shipment/{shipment_id}/details"
        )
        val rule = ExtractBasePathRule()
        val expected = Violation(rule.TITLE, rule.DESC_PATTERN.format("/shipment"), ViolationType.HINT, rule.RULE_URL,
                emptyList())
        assertThat(rule.validate(swagger)).isEqualTo(expected)
    }

    @Test
    fun multipleResourceNegativeCase() {
        val swagger = createSwaggerWithPaths(
                "/queue/models/configs/{config-id}",
                "/queue/models/",
                "/queue/models/{model-id}",
                "/queue/models/summaries"
        )
        val rule = ExtractBasePathRule()
        val expected = Violation(rule.TITLE, rule.DESC_PATTERN.format("/queue/models"), ViolationType.HINT,
                rule.RULE_URL, emptyList())
        assertThat(rule.validate(swagger)).isEqualTo(expected)
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