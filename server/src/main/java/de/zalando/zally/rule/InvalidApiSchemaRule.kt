package de.zalando.zally.rule

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.io.Resources
import com.typesafe.config.Config
import de.zalando.zally.dto.ViolationType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.IOException
import java.net.URL

@Component
open class InvalidApiSchemaRule(@Autowired rulesConfig: Config) : JsonRule() {

    private val log = LoggerFactory.getLogger(InvalidApiSchemaRule::class.java)

    override val title = "OpenAPI 2.0 schema"
    override val violationType = ViolationType.MUST
    override val url = "http://zalando.github.io/restful-api-guidelines/general-guidelines/GeneralGuidelines.html" +
            "#must-provide-api-reference-definition-using-openapi"
    override val code = "M000"
    open val description = "Given file is not OpenAPI 2.0 compliant."

    val jsonSchemaValidator: JsonSchemaValidator

    init {
        jsonSchemaValidator = getSchemaValidator(rulesConfig.getConfig(name))
    }

    private fun getSchemaValidator(ruleConfig: Config): JsonSchemaValidator {
        val schemaUrlProperty = "swagger_schema_url"
        if (!ruleConfig.hasPath(schemaUrlProperty)) {
            return getSchemaValidatorFromResource()
        }

        val swaggerSchemaUrl = URL(ruleConfig.getString(schemaUrlProperty))
        try {
            val schema = ObjectMapper().readTree(swaggerSchemaUrl)
            return JsonSchemaValidator(schema)
        } catch (ex: IOException) {
            log.warn("Unable to load swagger schema using URL: '$swaggerSchemaUrl' ${ex.message}. Using schema from resources.")
            return getSchemaValidatorFromResource()
        }
    }

    private fun getSchemaValidatorFromResource(): JsonSchemaValidator {
        val referencedOnlineSchema = "http://json-schema.org/draft-04/schema"
        val localResource = Resources.getResource("schemas/json-schema.json").toString()

        val schemaUrl = Resources.getResource("schemas/swagger-schema.json")
        val schema = ObjectTreeReader().readJson(schemaUrl)
        return JsonSchemaValidator(schema, schemaRedirects = mapOf(referencedOnlineSchema to localResource))
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
