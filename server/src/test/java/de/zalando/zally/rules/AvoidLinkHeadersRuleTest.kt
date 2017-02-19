package de.zalando.zally.rules

import de.zalando.zally.ViolationType
import de.zalando.zally.getFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class AvoidLinkHeadersRuleTest {

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

    @Test
    fun negativeCase() {
        val swagger = getFixture("avoidLinkHeaderRuleInvalid.json")
        val violation = AvoidLinkHeadersRule().validate(swagger)!!
        assertThat(violation.violationType).isEqualTo(ViolationType.MUST)
        assertThat(violation.paths).hasSameElementsAs(
                Arrays.asList("/product-put-requests/{product_path} Link", "/products Link"))

    }
}
