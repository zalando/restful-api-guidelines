package de.zalando.zally.rules

import de.zalando.zally.getFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class UseProblemJsonRuleTest {

    @Test
    fun positiveCaseSpa() {
        val swagger = getFixture("api_spa.yaml")
        assertThat(UseProblemJsonRule().validate(swagger)).isNull()
    }

    @Test
    fun negativeCaseSpp() {
        val swagger = getFixture("api_spp.json")
        val result = UseProblemJsonRule().validate(swagger)!!
        assertThat(result.paths).hasSameElementsAs(listOf(""))
    }
}
