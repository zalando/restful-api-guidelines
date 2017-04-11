package de.zalando.zally.rules

import de.zalando.zally.getFixture
import de.zalando.zally.testConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LimitNumberOfResourcesRuleTest {

    @Test
    fun positiveCase() {
        val swagger = getFixture("limitNumberOfResourcesValid.json")
        assertThat(LimitNumberOfResourcesRule(testConfig).validate(swagger)).isNull()
    }

    @Test
    fun negativeCase() {
        val swagger = getFixture("limitNumberOfResourcesInvalid.json")
        val result = LimitNumberOfResourcesRule(testConfig).validate(swagger)!!
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
