package de.zalando.zally.rules

import de.zalando.zally.getFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class NestedPathsCouldBeRootPathsRuleTest {

    @Test
    fun avoidLinkHeadersValidJson() {
        val swagger = getFixture("api_spp.json")
        val result = NestedPathsCouldBeRootPathsRule().validate(swagger)!!
        assertThat(result.paths).hasSameElementsAs(listOf(""))
    }

    @Test
    fun avoidLinkHeadersValidYaml() {
        val swagger = getFixture("api_spa.yaml")
        assertThat(NestedPathsCouldBeRootPathsRule().validate(swagger)).isNull()
    }
}
