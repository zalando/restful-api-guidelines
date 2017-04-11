package de.zalando.zally.rules

import de.zalando.zally.getFixture
import de.zalando.zally.testConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AvoidJavascriptKeywordsRuleTest {

    @Test
    fun positiveCase() {
        assertThat(AvoidJavascriptKeywordsRule(testConfig).validate(getFixture("avoidJavascriptValid.json"))).isNull()
    }

    @Test
    fun negativeCase() {
        val result = AvoidJavascriptKeywordsRule(testConfig).validate(getFixture("avoidJavascriptInvalid.json"))!!
        assertThat(result.paths).hasSameElementsAs(listOf("#/definitions/Pet"))
        assertThat(result.description).contains("return", "typeof")
    }
}
