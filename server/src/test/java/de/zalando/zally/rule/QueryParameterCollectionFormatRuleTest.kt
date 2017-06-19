package de.zalando.zally.rule

import de.zalando.zally.violation.ViolationType
import io.swagger.models.Swagger
import io.swagger.models.parameters.QueryParameter
import org.assertj.core.api.Assertions
import org.junit.Test

class QueryParameterCollectionFormatRuleTest {

    @Test
    fun negativeCaseCollectionFormatNotSupported() {
        val swagger = Swagger().apply {
            parameters = mapOf("test" to QueryParameter().apply { name = "test"; type = "array"; collectionFormat = "notSupported" })
        }

        val result = QueryParameterCollectionFormatRule().validate(swagger)!!
        Assertions.assertThat(result.violationType).isEqualTo(ViolationType.SHOULD)
        Assertions.assertThat(result.rule.code).isEqualTo("S011")
    }

    @Test
    fun negativeCaseCollectionFormatNull() {
        val swagger = Swagger().apply {
            parameters = mapOf("test" to QueryParameter().apply { name = "test"; type = "array"; collectionFormat = null })
        }

        val result = QueryParameterCollectionFormatRule().validate(swagger)!!
        Assertions.assertThat(result.violationType).isEqualTo(ViolationType.SHOULD)
        Assertions.assertThat(result.rule.code).isEqualTo("S011")
    }

    @Test
    fun positiveCaseCsv() {
        val swagger = Swagger().apply {
            parameters = mapOf("test" to QueryParameter().apply { name = "test"; type = "array"; collectionFormat = "csv" })
        }

        Assertions.assertThat(QueryParameterCollectionFormatRule().validate(swagger)).isNull()
    }

    @Test
    fun positiveCaseMulti() {
        val swagger = Swagger().apply {
            parameters = mapOf("test" to QueryParameter().apply { name = "test"; type = "array"; collectionFormat = "multi" })
        }

        Assertions.assertThat(QueryParameterCollectionFormatRule().validate(swagger)).isNull()
    }

}