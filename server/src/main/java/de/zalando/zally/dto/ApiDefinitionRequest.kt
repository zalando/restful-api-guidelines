package de.zalando.zally.dto

import com.fasterxml.jackson.annotation.JsonRawValue
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import de.zalando.zally.util.JsonRawValueDeserializer
import org.apache.commons.io.IOUtils

data class ApiDefinitionRequest (

    @JsonRawValue
    @JsonDeserialize(using = JsonRawValueDeserializer::class)
    var apiDefinition: String? = null,

    var apiDefinitionUrl: String? = null
) {
    companion object Factory {

        // for java invocations: it doesn't have overloaded constructors
        fun fromJson(json: String) = ApiDefinitionRequest(json)

        fun fromUrl(url: String) = ApiDefinitionRequest(apiDefinitionUrl = url)

        fun fromJsonResource(resource: String) = fromJson(
                IOUtils.toString(ClassLoader.getSystemResourceAsStream(resource))
        )
    }
}
