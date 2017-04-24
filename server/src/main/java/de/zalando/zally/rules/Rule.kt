package de.zalando.zally.rules

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Swagger

interface Rule {

    val title: String
    @get:JsonProperty("type") val violationType: ViolationType
    val url: String?
    val code: String
    @get:JsonIgnore val name: String

    fun validate(swagger: Swagger): Violation?
}
