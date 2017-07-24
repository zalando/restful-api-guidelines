package de.zalando.zally.rule

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.typesafe.config.Config
import de.zalando.zally.dto.ViolationType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.net.URL

@Component
open class InvalidApiSchemaRule(@Autowired val rulesConfig: Config) : JsonRule() {

    override val title = "OpenAPI 2.0 schema"
    override val violationType = ViolationType.MUST
    override val url = "http://zalando.github.io/restful-api-guidelines/general-guidelines/GeneralGuidelines.html" +
            "#must-provide-api-reference-definition-using-openapi"
    override val code = "M000"
    open val description = "Given file is not OpenAPI 2.0 compliant."

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

    fun getGeneralViolation(): Violation =
            Violation(this, title, description, violationType, url, emptyList())
}
