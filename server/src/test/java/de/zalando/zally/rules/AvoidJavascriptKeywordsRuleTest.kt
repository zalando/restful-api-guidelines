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
        val result = AvoidJavascriptKeywordsRule().validate(getFixture("avoidJavascriptInvalid.json"))!!
        assertThat(result.paths).hasSameElementsAs(listOf("#/definitions/Pet"))
        assertThat(result.description).contains("return", "typeof")
    }
}
