package de.zalando.zally.rules

import com.typesafe.config.ConfigFactory
import de.zalando.zally.getFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AvoidJavascriptKeywordsRuleTest {
    val ruleConfig = ConfigFactory.load("rules-config-test.conf")

    @Test
    fun positiveCase() {
        assertThat(AvoidJavascriptKeywordsRule(ruleConfig).validate(getFixture("avoidJavascriptValid.json"))).isNull()
    }

    @Test
    fun negativeCase() {
        val result = AvoidJavascriptKeywordsRule(ruleConfig).validate(getFixture("avoidJavascriptInvalid.json"))!!
        assertThat(result.paths).hasSameElementsAs(listOf("#/definitions/Pet"))
        assertThat(result.description).contains("return", "typeof")
    }
}
