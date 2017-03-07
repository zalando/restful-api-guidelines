package de.zalando.zally.rules

import de.zalando.zally.Violation
import io.swagger.models.Swagger

interface Rule {

    fun getName(): String

    fun validate(swagger: Swagger): Violation?
}
