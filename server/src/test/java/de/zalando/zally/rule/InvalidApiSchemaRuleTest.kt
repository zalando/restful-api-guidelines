package de.zalando.zally.rule

import com.typesafe.config.ConfigFactory
import de.zalando.zally.getResourceJson
import de.zalando.zally.testConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class InvalidApiSchemaRuleTest {

    companion object {
        val invalidSchemaRule = InvalidApiSchemaRule(testConfig)
    }

    @Test
    fun shouldNotFailOnCorrectYaml() {
        listOf("all_definitions.yaml", "api_spa.yaml", "api_without_scopes_defined.yaml").forEach { filePath ->
            val swaggerJson = getResourceJson(filePath)
            val validations = invalidSchemaRule.validate(swaggerJson)
            assertThat(validations).hasSize(0)
        }
    }

    @Test
    fun shouldNotFailOnCorrectJson() {
        listOf("api_spp.json", "snakeCaseForQueryParamsInvalidLocalParam.json", "limitNumberOfResourcesValid.json").forEach { filePath ->
            val swaggerJson = getResourceJson(filePath)
            val validations = invalidSchemaRule.validate(swaggerJson)
            assertThat(validations).hasSize(0)
        }
    }

    @Test
    fun shouldProduceViolationsAnyOfOneOf() {
        val swaggerJson = getResourceJson("api_tinbox.yaml")
        val validations = invalidSchemaRule.validate(swaggerJson)
        assertThat(validations).hasSize(2)
        assertThat(validations[0].description).isEqualTo("""instance failed to match at least one required schema among 2""")
        assertThat(validations[0].paths[0]).isEqualTo("/definitions/ConfigReviewStatusEntityJson/properties/date/type")
        assertThat(validations[1].paths[0]).isEqualTo("/securityDefinitions/tinbox")
    }

    @Test
    fun shouldProduceViolationsRequiredProperties() {
        val swaggerJson = getResourceJson("common_fields_invalid.yaml")
        val validations = invalidSchemaRule.validate(swaggerJson)
        assertThat(validations).hasSize(1)
        assertThat(validations[0].description).isEqualTo("""object has missing required properties (["paths"])""")
    }

    @Test
    fun shouldProduceViolationsNotAllowedProperties() {
        val swaggerJson = getResourceJson("successResponseAsJsonObjectValid.json")
        val validations = invalidSchemaRule.validate(swaggerJson)
        assertThat(validations).hasSize(3)
        assertThat(validations[0].description).isEqualTo("""object instance has properties ["anyOf"] which are not allowed by the schema: #/definitions/schema""")
        assertThat(validations[1].description).isEqualTo("""object instance has properties ["oneOf"] which are not allowed by the schema: #/definitions/schema""")
        assertThat(validations[2].description).isEqualTo("""instance failed to match exactly one schema of: #/definitions/parameter; #/definitions/jsonReference""")
        assertThat(validations[0].paths[0]).isEqualTo("/definitions/PetAnyOf")
        assertThat(validations[1].paths[0]).isEqualTo("/definitions/PetOneOf")
        assertThat(validations[2].paths[0]).isEqualTo("/paths/~1pets/post/parameters/1")
    }

    @Test
    fun shouldLoadSchemaFromResourceIfUrlNotSpecified() {
        val config = ConfigFactory.parseString("""
        InvalidApiSchemaRule {
             // swagger_schema_url not defined
        }
        """)

        val swaggerJson = getResourceJson("common_fields_invalid.yaml")
        val validations = InvalidApiSchemaRule(config).validate(swaggerJson)
        assertThat(validations).hasSize(1)
        assertThat(validations[0].description).isEqualTo("""object has missing required properties (["paths"])""")

    }

    @Test
    fun shouldLoadSchemaFromResourceIfLoadFromUrlFailed() {
        val config = ConfigFactory.parseString("""
        InvalidApiSchemaRule {
             swagger_schema_url: "http://localhost/random_url.html"
        }
        """)

        val swaggerJson = getResourceJson("common_fields_invalid.yaml")
        val validations = InvalidApiSchemaRule(config).validate(swaggerJson)
        assertThat(validations).hasSize(1)
        assertThat(validations[0].description).isEqualTo("""object has missing required properties (["paths"])""")

    }

}
