package de.zalando.zally.rule

import de.zalando.zally.getFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class UseProblemJsonRuleTest {

    @Test
    fun shouldReturnNoViolationsWhenErrorsReferencingToProblemJson() {
        val swagger = getFixture("problem_json.yaml")
        assertThat(UseProblemJsonRule().validate(swagger)).isNull()
    }

    @Test
    fun shouldReturnViolationsWhenErrorsReferencingToProblemJsonButNotProducingJson() {
        val swagger = getFixture("problem_json_not_produces_json.yaml")
        val result = UseProblemJsonRule().validate(swagger)!!
        assertThat(result.paths).hasSameElementsAs(listOf(
            "/products GET 400",
            "/products GET 401",
            "/products GET 403",
            "/products GET 404",
            "/products GET 405"
        ))
    }

    @Test
    fun shouldReturnNoViolationsWhenOperationsAreProducingJson() {
        val swagger = getFixture("problem_json_operations_produce_json.yaml")
        assertThat(UseProblemJsonRule().validate(swagger)).isNull()
    }

    @Test
    fun shouldReturnViolationsWhenCustomReferenceIsUsed() {
        val swagger = getFixture("api_tinbox.yaml")
        val result = UseProblemJsonRule().validate(swagger)!!
        assertThat(result.paths).hasSameElementsAs(listOf(
            "/merchants GET 400",
            "/merchants GET 401",
            "/merchants GET 403",
            "/merchants GET 404",
            "/merchants GET 502",
            "/merchants GET 504",
            "/meta/article_domains GET 400",
            "/meta/article_domains GET 401",
            "/meta/article_domains GET 403",
            "/meta/article_domains GET 404",
            "/meta/article_domains GET 502",
            "/meta/article_domains GET 504",
            "/meta/colors GET 400",
            "/meta/colors GET 401",
            "/meta/colors GET 403",
            "/meta/colors GET 404",
            "/meta/colors GET 502",
            "/meta/colors GET 504",
            "/meta/commodity_groups GET 400",
            "/meta/commodity_groups GET 401",
            "/meta/commodity_groups GET 403",
            "/meta/commodity_groups GET 404",
            "/meta/commodity_groups GET 502",
            "/meta/commodity_groups GET 504",
            "/meta/size_grids GET 400",
            "/meta/size_grids GET 401",
            "/meta/size_grids GET 403",
            "/meta/size_grids GET 404",
            "/meta/size_grids GET 502",
            "/meta/size_grids GET 504",
            "/meta/tags GET 400",
            "/meta/tags GET 401",
            "/meta/tags GET 403",
            "/meta/tags GET 404",
            "/meta/tags GET 502",
            "/meta/tags GET 504",
            "/queue/configs/{config-id} GET 400",
            "/queue/configs/{config-id} GET 401",
            "/queue/configs/{config-id} GET 403",
            "/queue/configs/{config-id} GET 404",
            "/queue/configs/{config-id} GET 502",
            "/queue/configs/{config-id} GET 504",
            "/queue/models GET 400",
            "/queue/models GET 401",
            "/queue/models GET 403",
            "/queue/models GET 404",
            "/queue/models GET 502",
            "/queue/models GET 504",
            "/queue/models/{model-id} GET 400",
            "/queue/models/{model-id} GET 401",
            "/queue/models/{model-id} GET 403",
            "/queue/models/{model-id} GET 404",
            "/queue/models/{model-id} GET 502",
            "/queue/models/{model-id} GET 504",
            "/queue/summaries GET 400",
            "/queue/summaries GET 401",
            "/queue/summaries GET 403",
            "/queue/summaries GET 404",
            "/queue/summaries GET 502",
            "/queue/summaries GET 504",
            "/rejections POST 400",
            "/rejections POST 401",
            "/rejections POST 403",
            "/rejections POST 502",
            "/rejections POST 504"
        ))
    }

    @Test
    fun shouldReturnViolationsWhenNoReferenceIsUsed() {
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
            "/products/{product_id}/children GET 418",
            "/products/{product_id}/children GET 429",
            "/products/{product_id}/updates/{update_id} GET 429",
            "/request-groups/{request_group_id}/summaries GET 429",
            "/request-groups/{request_group_id}/updates GET 429"
        ))
    }

    @Test
    fun shouldNotThrowExOnSchemasWithReferencesToEmptyDefinitions() {
        val swagger = getFixture("missing_definitions.yaml")
        val result = UseProblemJsonRule().validate(swagger)!!
        assertThat(result.paths).hasSameElementsAs(listOf(
                "/identifier-types/{identifier_type}/source-ids/{source_identifier} GET 401",
                "/identifier-types/{identifier_type}/source-ids/{source_identifier} GET 403",
                "/identifier-types/{identifier_type}/source-ids/{source_identifier} GET 404"
        ))
    }
}
