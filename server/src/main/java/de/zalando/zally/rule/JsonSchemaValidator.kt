package de.zalando.json.validation

import com.fasterxml.jackson.databind.JsonNode
import com.github.fge.jsonschema.cfg.ValidationConfiguration
import com.github.fge.jsonschema.core.exceptions.ProcessingException
import com.github.fge.jsonschema.core.report.ProcessingMessage
import com.github.fge.jsonschema.core.util.AsJson
import com.github.fge.jsonschema.main.JsonSchemaFactory
import com.github.fge.jsonschema.messages.JsonSchemaValidationBundle
import com.github.fge.msgsimple.bundle.MessageBundle
import com.github.fge.msgsimple.load.MessageBundles
import com.github.fge.msgsimple.source.PropertiesMessageSource
import java.io.IOException

class JsonSchemaValidator(val schema: JsonNode) {

    private val factory: JsonSchemaFactory

    private object Keywords {
        val oneOf = "oneOf"
        val anyOf = "anyOf"
        val additionalProperties = "additionalProperties"
    }

    init {
        val validationMessages = getValidationMessagesBundle()
        val validationConfiguration = ValidationConfiguration.newBuilder().setValidationMessages(validationMessages).freeze()

        factory = JsonSchemaFactory.newBuilder()
                .setValidationConfiguration(validationConfiguration).freeze()
    }

    private fun getValidationMessagesBundle(): MessageBundle {
        val customValidationMessages = PropertiesMessageSource.fromResource("/schema-validation-messages.properties")
        val validationMessages = MessageBundles.getBundle(JsonSchemaValidationBundle::class.java).thaw()
                .appendSource(customValidationMessages).freeze()
        return validationMessages
    }

    @Throws(ProcessingException::class, IOException::class)
    fun validate(swaggerSpec: JsonNode): JsonValidationResult {
        val validator = factory.validator

        val report = validator.validateUnchecked(schema, swaggerSpec, true)

        val messages = report
                .map(ProcessingMessage::asJson)
                .map(this::toValidationMessage)
                .toList()

        return JsonValidationResult(report.isSuccess, messages, (report as AsJson).asJson())
    }

    fun toValidationMessage(node: JsonNode): JsonValidationMessage {
        val keyword = node.path("keyword").textValue()
        val message = node.path("message").textValue()
        val specPath = node.at("/instance/pointer").textValue().let { if (it.isNullOrEmpty()) "/" else it }

        return when (keyword) {
            Keywords.oneOf, Keywords.anyOf -> createViolationMessageWithSchema(node, message, specPath, keyword)
            Keywords.additionalProperties -> createAdditionalPropertiesViolationMessage(node, message, specPath)
            else -> JsonValidationMessage(message, specPath)
        }
    }

    private fun createViolationMessageWithSchema(node: JsonNode, message: String, specPath: String, keyword: String): JsonValidationMessage {
        val schemaPath = node.at("/schema/pointer").textValue()
        if (!schemaPath.isNullOrBlank()) {
            val schemaRefNodes = schema.at(schemaPath + "/" + keyword)
            val schemaRefs = schemaRefNodes
                    .map { it.path("\$ref") }
                    .filterNot(JsonNode::isMissingNode)
                    .map(JsonNode::textValue)
                    .joinToString("; ")
            return JsonValidationMessage(message + schemaRefs, specPath)
        } else {
            return JsonValidationMessage(message, specPath)
        }
    }

    private fun createAdditionalPropertiesViolationMessage(node: JsonNode, message: String, specPath: String): JsonValidationMessage {
        val schemaPath = node.at("/schema/pointer").textValue()
        return JsonValidationMessage(message + schemaPath, specPath)
    }
}

data class JsonValidationResult(
        val isSuccess: Boolean,
        val messages: List<JsonValidationMessage>,
        val rawResult: JsonNode
)

data class JsonValidationMessage(
        val message: String,
        val path: String
)
