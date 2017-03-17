package de.zalando.zally.rules

import com.typesafe.config.ConfigFactory
import de.zalando.zally.getFixture
import de.zalando.zally.swaggerWithDefinitions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AvoidSynonymsRuleTest {
    val ruleConfig = ConfigFactory.load("rules-config.conf")

    @Test
    fun positiveCase() {
        val swagger = swaggerWithDefinitions("ExampleDefinition" to listOf("customer_id", "some_unique_prop_name"))
        assertThat(AvoidSynonymsRule(ruleConfig).validate(swagger)).isNull()
    }

    @Test
    fun multipleBadPropertiesInMultipleDefinitions() {
        val swagger = swaggerWithDefinitions(
                "Def1" to listOf("order_id", "c_id", "cust_id"),
                "Def2" to listOf("orderid")
        )
        val result = AvoidSynonymsRule(ruleConfig).validate(swagger)!!
        println(result.description)
        assertThat(result.description).contains("c_id", "cust_id", "orderid")
        assertThat(result.paths).hasSameElementsAs(listOf(
                "#/definitions/Def1",
                "#/definitions/Def2")
        )
    }

    @Test
    fun positiveCaseSpp() {
        val swagger = getFixture("api_spp.json")
        assertThat(AvoidSynonymsRule(ruleConfig).validate(swagger)).isNull()
    }

    @Test
    fun positiveCaseSpa() {
        val swagger = getFixture("api_spa.yaml")
        assertThat(AvoidSynonymsRule(ruleConfig).validate(swagger)).isNull()
    }
}
