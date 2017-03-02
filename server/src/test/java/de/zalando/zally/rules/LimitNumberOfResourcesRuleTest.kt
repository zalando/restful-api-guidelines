package de.zalando.zally.rules

import de.zalando.zally.getFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Unit test for limiting number of resources rule.
 */
class LimitNumberOfResourcesRuleTest {
    @Test
    fun positiveCase() {
        val swagger = getFixture("limitNumberOfResourcesValid.json")
        assertThat(LimitNumberOfResourcesRule().validate(swagger)).isNull()
    }

    @Test
    fun negativeCase() {
        val swagger = getFixture("limitNumberOfResourcesInvalid.json")
        assertThat(LimitNumberOfResourcesRule().validate(swagger)!!.paths).hasSameElementsAs(listOf(
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
