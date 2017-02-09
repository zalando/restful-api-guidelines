package de.zalando.zally.rules

import io.swagger.models.Path
import io.swagger.models.Swagger
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class KebabCaseInPathSegmentsRuleTest {

    private val testPath1 = "/shipment-order/{shipment_order_id}"
    private val testPath2 = "/partner-order/{partner_order_id}"
    private val testPath3 = "/partner-order/{partner_order_id}/partner-order/{partner_order_id}"
    private val wrongTestPath1 = "/shipment_order/{shipment_order_id}"
    private val wrongTestPath2 = "/partner-order/{partner_order_id}/partner-order1/{partner_order_id}"

    private fun createSwaggerWithPaths(vararg testPaths: String) =
            Swagger().apply {
                paths = testPaths.map { it to Path() }.toMap()
            }

    @Test
    fun validateEmptyPath() {
        assertThat(KebabCaseInPathSegmentsRule().validate(Swagger())).isEmpty()
    }

    @Test
    fun validateNormalPath() {
        val swagger = createSwaggerWithPaths(testPath1)
        assertThat(KebabCaseInPathSegmentsRule().validate(swagger)).isEmpty()
    }

    @Test
    fun validateMultipleNormalPaths() {
        val swagger = createSwaggerWithPaths(testPath1, testPath2, testPath3)
        assertThat(KebabCaseInPathSegmentsRule().validate(swagger)).isEmpty()
    }

    @Test
    fun validateFalsePath() {
        val swagger = createSwaggerWithPaths(wrongTestPath1)
        val result = KebabCaseInPathSegmentsRule().validate(swagger)
        assertThat(result.map { it.path.orElse(null) }).containsExactly(wrongTestPath1)
    }

    @Test
    fun validateMultipleFalsePaths() {
        val swagger = createSwaggerWithPaths(wrongTestPath1, testPath2, wrongTestPath2)
        val result = KebabCaseInPathSegmentsRule().validate(swagger)
        assertThat(result.map { it.path.orElse(null) }).containsExactlyInAnyOrder(wrongTestPath1, wrongTestPath2)
    }
}
