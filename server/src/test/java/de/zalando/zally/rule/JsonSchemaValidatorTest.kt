package de.zalando.zally.rule

import com.google.common.io.Resources
import org.assertj.core.api.Assertions
import org.junit.Test

class JsonSchemaValidatorTest {

    @Test
    fun shouldLoadSchemaFromResourcesWithRef() {
        val onlineSchema = "http://simple-referenced-schema.json/schema"
        val localResource = Resources.getResource("schemas/simple-referenced-schema.json").toString()

        val schemaUrl = Resources.getResource("schemas/simple-schema.json")
        val json = ObjectTreeReader().readJson(schemaUrl)
        val jsonSchemaValidator = JsonSchemaValidator(json, mapOf(onlineSchema to localResource))

        val jsonToValidate = ObjectTreeReader().readJson("""
        {
            "firstName": "MyName",
            "lastName": "MyLastName",
            "age": -10
        }
        """)

        val valResult = jsonSchemaValidator.validate(jsonToValidate)
        Assertions.assertThat(valResult.isSuccess).isFalse()
        Assertions.assertThat(valResult.messages[0].message).isEqualTo("numeric instance is lower than the required minimum (minimum: 0, found: -10)")
    }

    @Test
    fun shouldLoadSwaggerSchemaFromResourcesWithRef() {
        val onlineSchema = "http://json-schema.org/draft-04/schema"
        val localResource = Resources.getResource("schemas/json-schema.json").toString()

        val schemaUrl = Resources.getResource("schemas/swagger-schema.json")
        val json = ObjectTreeReader().readJson(schemaUrl)
        var jsonSchemaValidator = JsonSchemaValidator(json, mapOf(onlineSchema to localResource))

        val specJson = ObjectTreeReader().readYaml(Resources.getResource("fixtures/api_tinbox.yaml"))

        val valResult = jsonSchemaValidator.validate(specJson)
        Assertions.assertThat(valResult.isSuccess).isFalse()
        Assertions.assertThat(valResult.messages[0].message).isEqualTo("instance failed to match at least one required schema among 2")
    }
}