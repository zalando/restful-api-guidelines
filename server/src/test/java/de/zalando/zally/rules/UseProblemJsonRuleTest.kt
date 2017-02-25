package de.zalando.zally.rules

import de.zalando.zally.getFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class UseProblemJsonRuleTest {

    @Test
    fun positiveCaseTinbox() {
        val swagger = getFixture("api_tinbox.yaml")
        assertThat(UseProblemJsonRule().validate(swagger)).isNull()
    }

    @Test
    fun negativeCaseSpp() {
        val swagger = getFixture("api_spp.json")
        val result = UseProblemJsonRule().validate(swagger)!!
        assertThat(result.paths).hasSameElementsAs(listOf(
                "/product-put-requests/{product_path} POST 400",
                "/product-put-requests/{product_path} POST 406",
                "/product-put-requests/{product_path} POST 413",
                "/product-put-requests/{product_path} POST 422",
                "/product-put-requests/{product_path} POST 429",
                "/products GET 429",
                "/products/{product_id} GET 418",
                "/products/{product_id} GET 429",
                "/products/{product_id} PATCH 400",
                "/products/{product_id} PATCH 429",
                "/products/{product_id} HEAD 404",
                "/products/{product_id} HEAD 429",
                "/products/{product_id}/children GET 418",
                "/products/{product_id}/children GET 429",
                "/products/{product_id}/updates/{update_id} GET 429",
                "/request-groups/{request_group_id}/summaries GET 429",
                "/request-groups/{request_group_id}/updates GET 429"
        ))
    }
}
