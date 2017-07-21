package de.zalando.zally.rule

import de.zalando.zally.dto.Violation
import de.zalando.zally.dto.ViolationType
import io.swagger.models.Swagger

interface Rule {

    val title: String
    val violationType: ViolationType
    val url: String?
    val code: String
    val name: String

    fun validate(swagger: Swagger): Violation?
}
