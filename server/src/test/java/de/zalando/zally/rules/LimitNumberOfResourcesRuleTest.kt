package de.zalando.zally.rules

import com.typesafe.config.ConfigFactory
import de.zalando.zally.getFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LimitNumberOfResourcesRuleTest {
    val ruleConfig = ConfigFactory.load("rules-config-test.conf")

    @Test
    fun positiveCase() {
        val swagger = getFixture("limitNumberOfResourcesValid.json")
        assertThat(LimitNumberOfResourcesRule(ruleConfig).validate(swagger)).isNull()
    }

    @Test
    fun negativeCase() {
        val swagger = getFixture("limitNumberOfResourcesInvalid.json")
        val result = LimitNumberOfResourcesRule(ruleConfig).validate(swagger)!!
        assertThat(result.paths).hasSameElementsAs(listOf(
                "/items",
                "/items/{item_id}",
                "/items10",
                "/items3",
                "/items4",
                "/items5",
                "/items6",
                "/items7",
                "/items8",
                "/items9"))
    }
}
