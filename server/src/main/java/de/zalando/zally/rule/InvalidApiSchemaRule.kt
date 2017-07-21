package de.zalando.zally.rule

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.typesafe.config.Config
import de.zalando.json.validation.JsonSchemaValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.net.URL

@Component
class InvalidApiSchemaRule(@Autowired val rulesConfig: Config) : InvalidApiSpecificationRule() {

    val jsonSchemaValidator: JsonSchemaValidator

    init {
        val swaggerSchemaUrl = URL(rulesConfig.getConfig(name).getString("swagger_schema_url"))
        val swaggerSchema = ObjectMapper().readTree(swaggerSchemaUrl)
        jsonSchemaValidator = JsonSchemaValidator(swaggerSchema)
    }

    override fun validate(swagger: JsonNode): List<Violation> {
        return jsonSchemaValidator.validate(swagger).let { validationResult ->
            validationResult.messages.map { message ->
                    Violation(this, this.title, message.message, this.violationType, this.url, listOf(message.path))
            }
        }
    }

}
