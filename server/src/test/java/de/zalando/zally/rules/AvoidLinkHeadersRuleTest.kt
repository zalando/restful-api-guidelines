package de.zalando.zally.rules

import com.typesafe.config.ConfigFactory
import de.zalando.zally.ViolationType
import de.zalando.zally.getFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AvoidLinkHeadersRuleTest {
    val ruleConfig = ConfigFactory.load("rules-config-test.conf")

    @Test
    fun positiveCaseSpp() {
        val swagger = getFixture("api_spp.json")
        assertThat(AvoidLinkHeadersRule(ruleConfig).validate(swagger)).isNull()
    }

    @Test
    fun positiveCaseSpa() {
        val swagger = getFixture("api_spa.yaml")
        assertThat(AvoidLinkHeadersRule(ruleConfig).validate(swagger)).isNull()
    }

    @Test
    fun negativeCase() {
        val swagger = getFixture("avoidLinkHeaderRuleInvalid.json")
        val violation = AvoidLinkHeadersRule(ruleConfig).validate(swagger)!!
        assertThat(violation.violationType).isEqualTo(ViolationType.MUST)
        assertThat(violation.paths).hasSameElementsAs(
                listOf("/product-put-requests/{product_path} Link", "/products Link"))

    }
}
