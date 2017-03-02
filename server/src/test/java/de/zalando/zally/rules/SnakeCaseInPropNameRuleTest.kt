package de.zalando.zally.rules

import io.swagger.models.ModelImpl
import io.swagger.models.Swagger
import io.swagger.models.properties.StringProperty
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SnakeCaseInPropNameRuleTest {

    private val testDefinition1 = ModelImpl()
    private val testDefinition2 = ModelImpl()
    private val testPorperty1 = StringProperty()
    private val testPorperty2 = StringProperty()

    private fun swaggerWithDefinitions(vararg defs: Pair<String, List<String>>): Swagger =
            Swagger().apply {
                definitions = defs.map { def ->
                    def.first to ModelImpl().apply {
                        properties = def.second.map { prop -> prop to StringProperty() }.toMap()
                    }
                }.toMap()
            }

    @Test
    fun emptySwagger() {
        assertThat(SnakeCaseInPropNameRule().validate(Swagger())).isNull()
    }

    @Test
    fun validateNormalProperty() {
        val swagger = swaggerWithDefinitions("ExampleDefinition" to listOf("test_property"))
        assertThat(SnakeCaseInPropNameRule().validate(swagger)).isNull()
    }

    @Test
    fun validateMultipleNormalProperties() {
        val swagger = swaggerWithDefinitions("ExampleDefinition" to listOf("test_property", "test_property_two"))
        assertThat(SnakeCaseInPropNameRule().validate(swagger)).isNull()
    }

    @Test
    fun validateMultipleNormalPropertiesInMultipleDefinitions() {
        val swagger = swaggerWithDefinitions(
                "ExampleDefinition" to listOf("test_property"),
                "ExampleDefinitionTwo" to listOf("test_property_two")
        )
        assertThat(SnakeCaseInPropNameRule().validate(swagger)).isNull()
    }

    @Test
    fun validateFalseProperty() {
        val swagger = swaggerWithDefinitions("ExampleDefinition" to listOf("TEST_PROPERTY"))
        val result = SnakeCaseInPropNameRule().validate(swagger)!!
        assertThat(result.description).contains("TEST_PROPERTY")
        assertThat(result.paths).hasSameElementsAs(listOf("#/definitions/ExampleDefinition"))
    }

    @Test
    fun validateMultipleFalsePropertiesInMultipleDefinitions() {
        val swagger = swaggerWithDefinitions(
                "ExampleDefinition" to listOf("TEST_PROPERTY"),
                "ExampleDefinitionTwo" to listOf("test_property_TWO")
        )
        val result = SnakeCaseInPropNameRule().validate(swagger)!!
        assertThat(result.description).contains("TEST_PROPERTY", "test_property_TWO")
        assertThat(result.paths).hasSameElementsAs(listOf(
                "#/definitions/ExampleDefinition",
                "#/definitions/ExampleDefinitionTwo")
        )
    }
}
