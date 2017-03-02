package de.zalando.zally.rules

import de.zalando.zally.swaggerWithPaths
import io.swagger.models.Swagger
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class KebabCaseInPathSegmentsRuleTest {

    private val testPath1 = "/shipment-order/{shipment_order_id}"
    private val testPath2 = "/partner-order/{partner_order_id}"
    private val testPath3 = "/partner-order/{partner_order_id}/partner-order/{partner_order_id}"
    private val wrongTestPath1 = "/shipment_order/{shipment_order_id}"
    private val wrongTestPath2 = "/partner-order/{partner_order_id}/partner-order1/{partner_order_id}"

    @Test
    fun emptySwagger() {
        assertThat(KebabCaseInPathSegmentsRule().validate(Swagger())).isNull()
    }

    @Test
    fun validateNormalPath() {
        val swagger = swaggerWithPaths(testPath1)
        assertThat(KebabCaseInPathSegmentsRule().validate(swagger)).isNull()
    }

    @Test
    fun validateMultipleNormalPaths() {
        val swagger = swaggerWithPaths(testPath1, testPath2, testPath3)
        assertThat(KebabCaseInPathSegmentsRule().validate(swagger)).isNull()
    }

    @Test
    fun validateFalsePath() {
        val swagger = swaggerWithPaths(wrongTestPath1)
        val result = KebabCaseInPathSegmentsRule().validate(swagger)!!
        assertThat(result.paths).hasSameElementsAs(listOf(wrongTestPath1))
    }

    @Test
    fun validateMultipleFalsePaths() {
        val swagger = swaggerWithPaths(wrongTestPath1, testPath2, wrongTestPath2)
        val result = KebabCaseInPathSegmentsRule().validate(swagger)!!
        assertThat(result.paths).hasSameElementsAs(listOf(wrongTestPath1, wrongTestPath2))
    }

}
