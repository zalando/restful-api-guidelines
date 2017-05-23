package de.zalando.zally.rule

import de.zalando.zally.getFixture
import de.zalando.zally.swaggerWithDefinitions
import de.zalando.zally.testConfig
import de.zalando.zally.testMetricRegistry
import de.zalando.zally.testMetricServices
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AvoidSynonymsRuleTest {

    @Test
    fun positiveCase() {
        val swagger = swaggerWithDefinitions("ExampleDefinition" to listOf("customer_id", "some_unique_prop_name"))
        assertThat(AvoidSynonymsRule(testConfig, testMetricServices).validate(swagger, true)).isNull()
    }

    @Test
    fun multipleBadPropertiesInMultipleDefinitions() {
        val swagger = swaggerWithDefinitions(
            "Def1" to listOf("order_id", "c_id", "cust_id"),
            "Def2" to listOf("orderid", "c_id")
        )
        val result = AvoidSynonymsRule(testConfig, testMetricServices).validate(swagger, true)!!
        println(result.description)
        assertThat(result.description).contains("c_id", "cust_id", "orderid")
        assertThat(result.paths).hasSameElementsAs(listOf(
            "#/definitions/Def1",
            "#/definitions/Def2")
        )
        assertThat(testMetricRegistry.histogram("histogram.rules.avoid-synonyms.synonym.c_id").snapshot.max)
            .isEqualTo(2)
        assertThat(testMetricRegistry.histogram("histogram.rules.avoid-synonyms.synonym.cust_id").snapshot.max)
            .isEqualTo(1)
        assertThat(testMetricRegistry.histogram("histogram.rules.avoid-synonyms.synonym.orderid").snapshot.max)
            .isEqualTo(1)
    }

    @Test
    fun positiveCaseSpp() {
        val swagger = getFixture("api_spp.json")
        assertThat(AvoidSynonymsRule(testConfig, testMetricServices).validate(swagger, true)).isNull()
    }

    @Test
    fun positiveCaseSpa() {
        val swagger = getFixture("api_spa.yaml")
        assertThat(AvoidSynonymsRule(testConfig, testMetricServices).validate(swagger, true)).isNull()
    }
}
