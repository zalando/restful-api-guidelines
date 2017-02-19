package de.zalando.zally.rules

import de.zalando.zally.getFixture
import de.zalando.zally.utils.PatternUtil.isApplicationJsonOrProblemJson
import de.zalando.zally.utils.PatternUtil.isCustomMediaTypeWithVersioning
import io.swagger.models.Operation
import io.swagger.models.Path
import io.swagger.models.Swagger
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class MediaTypesRuleTest {
    private val rule = MediaTypesRule()

    fun swaggerWithMediaTypes(vararg pathToMedia: Pair<String, List<String>>): Swagger =
            Swagger().apply {
                paths = pathToMedia.map {
                    val (path, types) = it
                    path to Path().apply {
                        set("get", Operation().apply { produces = types })
                    }
                }.toMap()
            }

    @Test
    fun isApplicationJsonOrProblemJsonForValidInput() {
        assertThat(isApplicationJsonOrProblemJson("application/json")).isTrue()
        assertThat(isApplicationJsonOrProblemJson("application/problem+json")).isTrue()
    }

    @Test
    fun isApplicationJsonOrProblemJsonForInvalidInput() {
        assertThat(isApplicationJsonOrProblemJson("application/vnd.api+json")).isFalse()
        assertThat(isApplicationJsonOrProblemJson("application/x.zalando.contract+json")).isFalse()
    }

    @Test
    fun isCustomMediaTypeWithVersioningForValidInput() {
        assertThat(isCustomMediaTypeWithVersioning("application/vnd.api+json;v=12")).isTrue()
        assertThat(isCustomMediaTypeWithVersioning("application/x.zalando.contract+json;v=34")).isTrue()
        assertThat(isCustomMediaTypeWithVersioning("application/vnd.api+json;version=123")).isTrue()
        assertThat(isCustomMediaTypeWithVersioning("application/x.zalando.contract+json;version=345")).isTrue()
    }

    @Test
    fun isCustomMediaTypeWithVersioningForInvalidInput() {
        assertThat(isCustomMediaTypeWithVersioning("application/vnd.api+json")).isFalse()
        assertThat(isCustomMediaTypeWithVersioning("application/x.zalando.contract+json")).isFalse()
        assertThat(isCustomMediaTypeWithVersioning("application/vnd.api+json;ver=1")).isFalse()
        assertThat(isCustomMediaTypeWithVersioning("application/x.zalando.contract+json;v:1")).isFalse()
        assertThat(isCustomMediaTypeWithVersioning("application/vnd.api+json;version=")).isFalse()
        assertThat(isCustomMediaTypeWithVersioning("application/x.zalando.contract+json;")).isFalse()
    }

    @Test
    fun emptySwagger() {
        assertThat(rule.validate(Swagger())).isNull()
    }

    @Test
    fun positiveCase() {
        val swagger = swaggerWithMediaTypes(
                "/shipment-order/{shipment_order_id}" to listOf(
                        "application/x.zalando.contract+json;v=123",
                        "application/vnd.api+json;version=3"))
        assertThat(rule.validate(swagger)).isNull()
    }

    @Test
    fun negativeCase() {
        val path = "/shipment-order/{shipment_order_id}"
        val swagger = swaggerWithMediaTypes(path to listOf("application/json", "application/vnd.api+json"))
        assertThat(rule.validate(swagger)!!.paths).hasSameElementsAs(listOf("$path GET"))
    }

    @Test
    fun multiplePaths() {
        val swagger = swaggerWithMediaTypes(
                "/path1" to listOf("application/json", "application/vnd.api+json"),
                "/path2" to listOf("application/x.zalando.contract+json"),
                "/path3" to listOf("application/x.zalando.contract+json;v=123")
        )
        val result = rule.validate(swagger)!!
        println(result)
        assertThat(result.paths).hasSameElementsAs(listOf(
                "/path1 GET",
                "/path2 GET"
        ))
    }

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
}
