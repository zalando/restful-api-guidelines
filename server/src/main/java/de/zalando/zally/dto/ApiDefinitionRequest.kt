package de.zalando.zally.dto

import com.fasterxml.jackson.annotation.JsonRawValue
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import de.zalando.zally.util.JsonRawValueDeserializer

data class ApiDefinitionRequest (

    @JsonRawValue
    @JsonDeserialize(using = JsonRawValueDeserializer::class)
    var apiDefinition: String? = null,

    var apiDefinitionUrl: String? = null
)
