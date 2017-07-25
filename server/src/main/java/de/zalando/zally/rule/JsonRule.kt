package de.zalando.zally.rule

import com.fasterxml.jackson.databind.JsonNode

abstract class JsonRule : AbstractRule() {

    fun accepts(swagger: JsonNode): Boolean {
        val ignoredCodes = swagger.get(zallyIgnoreExtension)
        return ignoredCodes == null
                || !ignoredCodes.isArray
                || code !in ignoredCodes.map { it.asText() }
    }

    abstract fun validate(swagger: JsonNode): Iterable<Violation>

}
