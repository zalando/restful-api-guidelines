package de.zalando.zally.violation

import com.fasterxml.jackson.annotation.JsonRawValue
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import de.zalando.zally.util.JsonRawValueDeserializer

/**
 * @author innokenty
 */
data class ApiDefinitionRequest (

    @JsonRawValue
    @JsonDeserialize(using = JsonRawValueDeserializer::class)
    var apiDefinition: String? = null,

    var apiDefinitionUrl: String? = null
)
