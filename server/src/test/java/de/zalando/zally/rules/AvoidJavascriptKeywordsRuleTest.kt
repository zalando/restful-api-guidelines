package de.zalando.zally.rules

import de.zalando.zally.getFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class AvoidJavascriptKeywordsRuleTest {

    @Test
    fun positiveCase() {
        assertThat(AvoidJavascriptKeywordsRule().validate(getFixture("avoidJavascriptValid.json"))).isNull()
    }

    @Test
    fun negativeCase() {
        val results = AvoidJavascriptKeywordsRule().validate(getFixture("avoidJavascriptInvalid.json"))!!.paths
        assertThat(results).hasSameElementsAs(Arrays.asList("Pet.return", "Pet.typeof"))
    }
}
