package de.zalando.zally.rule

import com.fasterxml.jackson.databind.JsonNode
import com.github.fge.jsonschema.cfg.ValidationConfiguration
import com.github.fge.jsonschema.core.exceptions.ProcessingException
import com.github.fge.jsonschema.core.load.configuration.LoadingConfiguration
import com.github.fge.jsonschema.core.load.uri.URITranslatorConfiguration
import com.github.fge.jsonschema.core.report.ProcessingMessage
import com.github.fge.jsonschema.main.JsonSchemaFactory
import com.github.fge.jsonschema.messages.JsonSchemaValidationBundle
import com.github.fge.msgsimple.bundle.MessageBundle
import com.github.fge.msgsimple.load.MessageBundles
import com.github.fge.msgsimple.source.PropertiesMessageSource
import java.io.IOException

class JsonSchemaValidator(val schema: JsonNode, schemaRedirects: Map<String, String> = mapOf()) {

    data class ValidationResult(
            val isSuccess: Boolean,
            val messages: List<ValidationMessage>
    )

    data class ValidationMessage(
            val message: String,
            val path: String
    )

    private object Keywords {
        val oneOf = "oneOf"
        val anyOf = "anyOf"
        val additionalProperties = "additionalProperties"
    }

    private val factory: JsonSchemaFactory

    init {
        factory = createValidatorFactory(schemaRedirects)
    }

    @Throws(ProcessingException::class, IOException::class)
    fun validate(jsonToValidate: JsonNode): ValidationResult {
        val validator = factory.validator
        val report = validator.validateUnchecked(schema, jsonToValidate, true)
        val messages = report
                .map(this::toValidationMessage)
                .toList()
        return ValidationResult(report.isSuccess, messages)
    }

    private fun toValidationMessage(processingMessage: ProcessingMessage): ValidationMessage {
        val node = processingMessage.asJson()
        val keyword = node.path("keyword").textValue()
        val message = node.path("message").textValue()
        val specPath = node.at("/instance/pointer").textValue().let { if (it.isNullOrEmpty()) "/" else it }

        return when (keyword) {
            Keywords.oneOf, Keywords.anyOf -> createValidationMessageWithSchemaRefs(node, message, specPath, keyword)
            Keywords.additionalProperties -> createValidationMessageWithSchemaPath(node, message, specPath)
            else -> ValidationMessage(message, specPath)
        }
    }

    private fun createValidationMessageWithSchemaRefs(node: JsonNode, message: String, specPath: String, keyword: String): ValidationMessage {
        val schemaPath = node.at("/schema/pointer").textValue()
        if (!schemaPath.isNullOrBlank()) {
            val schemaRefNodes = schema.at(schemaPath + "/" + keyword)
            val schemaRefs = schemaRefNodes
                    .map { it.path("\$ref") }
                    .filterNot(JsonNode::isMissingNode)
                    .map(JsonNode::textValue)
                    .joinToString("; ")
            return ValidationMessage(message + schemaRefs, specPath)
        } else {
            return ValidationMessage(message, specPath)
        }
    }

    private fun createValidationMessageWithSchemaPath(node: JsonNode, message: String, specPath: String): ValidationMessage {
        val schemaPath = node.at("/schema/pointer").textValue()
        return ValidationMessage(message + schemaPath, specPath)
    }

    private fun createValidatorFactory(schemaRedirects: Map<String, String>): JsonSchemaFactory {
        val validationMessages = getValidationMessagesBundle()
        val validationConfiguration = ValidationConfiguration.newBuilder()
                .setValidationMessages(validationMessages)
                .freeze()

        val loadingConfig = createLoadingConfiguration(schemaRedirects)

        return JsonSchemaFactory.newBuilder()
                .setValidationConfiguration(validationConfiguration)
                .setLoadingConfiguration(loadingConfig)
                .freeze()
    }

    private fun createLoadingConfiguration(schemaRedirects: Map<String, String>): LoadingConfiguration? {
        val urlTranslatorConfig = URITranslatorConfiguration.newBuilder()
        schemaRedirects.forEach { (from, to) -> urlTranslatorConfig.addSchemaRedirect(from, to) }

        val loadingConfig = LoadingConfiguration.newBuilder()
                .setURITranslatorConfiguration(urlTranslatorConfig.freeze())
                .freeze()
        return loadingConfig
    }

    private fun getValidationMessagesBundle(): MessageBundle {
        val customValidationMessages = PropertiesMessageSource.fromResource("/schema-validation-messages.properties")
        val validationMessages = MessageBundles.getBundle(JsonSchemaValidationBundle::class.java).thaw()
                .appendSource(customValidationMessages).freeze()
        return validationMessages
    }
}