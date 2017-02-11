package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.ModelImpl
import io.swagger.models.Swagger
import io.swagger.models.properties.StringProperty
import io.swagger.parser.SwaggerParser
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SnakeCaseInPropNameRuleTest {

    private val rule = SnakeCaseInPropNameRule()

    private fun createSwagger(models: Map<String, List<String>>) =
            Swagger().apply {
                definitions = models.mapValues { names ->
                    val props = names.value.map { Pair(it, StringProperty()) }.toMap()
                    ModelImpl().apply { properties = props }
                }
            }

    @Test
    fun validateEmptySwagger() {
        assertThat(rule.validate(Swagger())).isEmpty()
    }

    @Test
    fun validateNormalProperty() {
        val swagger = createSwagger(mapOf("Definition1" to listOf("test_property")))
        assertThat(rule.validate(swagger)).isEmpty()
    }

    @Test
    fun validateMultipleNormalProperties() {
        val swagger = createSwagger(mapOf("Definition1" to listOf("test_property", "test_property_two")))
        assertThat(rule.validate(swagger)).isEmpty()
    }

    @Test
    fun validateMultipleNormalPropertiesInMultipleDefinitions() {
        val swagger = createSwagger(mapOf(
                "Definition1" to listOf("some_name", "another_valid_camel_case"),
                "Definition2" to listOf("again", "prop_1")
        ))
        assertThat(rule.validate(swagger)).isEmpty()
    }

    @Test
    fun validateFalseProperty() {
        val swagger = createSwagger(mapOf(
                "Definition1" to listOf("TEST_PROPERTY")
        ))
        val result = rule.validate(swagger)
        assertThat(result).hasSameElementsAs(listOf(
                Violation(rule.title, rule.description.format("TEST_PROPERTY"), ViolationType.MUST, rule.ruleLink, "Definition1")
        ))
    }

    @Test
    fun validateMultipleFalsePropertiesInMultipleDefinitions() {
        val swagger = createSwagger(mapOf(
                "Definition1" to listOf("OTHER_PROP_wrong"),
                "Definition2" to listOf("test_property_TWO", "_again_not_right")
        ))
        val result = rule.validate(swagger)
        assertThat(result).hasSameElementsAs(listOf(
                Violation(rule.title, rule.description.format("OTHER_PROP_wrong"), ViolationType.MUST, rule.ruleLink, "Definition1"),
                Violation(rule.title, rule.description.format("test_property_TWO"), ViolationType.MUST, rule.ruleLink, "Definition2"),
                Violation(rule.title, rule.description.format("_again_not_right"), ViolationType.MUST, rule.ruleLink, "Definition2")
        ))
    }

    @Test
    fun linksWhitelisted() {
        val swagger = createSwagger(mapOf(
                "Definition1" to listOf("_links")
        ))
        assertThat(rule.validate(swagger)).isEmpty()
    }

    @Test
    fun positiveCaseSpp() {
        val swagger = SwaggerParser().read("api_spp.json")
        assertThat(PascalCaseHttpHeadersRule().validate(swagger)).isEmpty()
    }

    @Test
    fun positiveCaseTinbox() {
        val swagger = SwaggerParser().read("api_tinbox.yaml")
        assertThat(PascalCaseHttpHeadersRule().validate(swagger)).isEmpty()
    }
}
