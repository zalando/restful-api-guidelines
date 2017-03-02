package de.zalando.zally.rules

import de.zalando.zally.getFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AvoidJavascriptKeywordsRuleTest {

    @Test
    fun positiveCase() {
        assertThat(AvoidJavascriptKeywordsRule().validate(getFixture("avoidJavascriptValid.json"))).isNull()
    }

    @Test
    fun negativeCase() {
        val results = AvoidJavascriptKeywordsRule().validate(getFixture("avoidJavascriptInvalid.json"))!!.paths
        assertThat(results).hasSameElementsAs(listOf("Pet.return", "Pet.typeof"))
    }
}
