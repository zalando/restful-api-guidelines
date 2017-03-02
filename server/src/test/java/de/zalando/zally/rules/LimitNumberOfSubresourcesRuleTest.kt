package de.zalando.zally.rules

import de.zalando.zally.getFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LimitNumberOfSubresourcesRuleTest {
    @Test
    fun positiveCase() {
        val swagger = getFixture("limitNumberOfSubresourcesValid.json")
        assertThat(LimitNumberOfSubresourcesRule().validate(swagger)).isNull()
    }

    @Test
    fun negativeCase() {
        val swagger = getFixture("limitNumberOfSubresourcesInvalid.json")
        assertThat(LimitNumberOfSubresourcesRule().validate(swagger)!!.paths).hasSameElementsAs(
                listOf("/items/{some_id}/item_level_1/{level1_id}/item-level-2/{level2_id}/item-level-3/{level3_id}/item-level-4")
        )
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
