package de.zalando.zally.rules

import de.zalando.zally.getFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class NestedPathsMayBeRootPathsRuleTest {

    @Test
    fun avoidLinkHeadersValidJson() {
        val swagger = getFixture("api_spp.json")
        val result = NestedPathsMayBeRootPathsRule().validate(swagger)!!
        assertThat(result.paths).hasSameElementsAs(listOf("/products/{product_id}/updates/{update_id}"))
    }

    @Test
    fun avoidLinkHeadersValidYaml() {
        val swagger = getFixture("api_spa.yaml")
        assertThat(NestedPathsMayBeRootPathsRule().validate(swagger)).isNull()
    }
}
