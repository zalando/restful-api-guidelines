package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Swagger

interface Rule {

    val title: String
    val violationType: ViolationType
    val url: String?
    val code: String
    val name: String

    fun validate(swagger: Swagger): Violation?
}
