package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Swagger

class InvalidApiSchemaRule : AbstractRule() {
    override val title = "OpenAPI 2.0 schema"
    override val violationType = ViolationType.MUST
    override val url = ""
    override val code = "M000"
    val description = "Given file is not OpenAPI 2.0 compliant."

    override fun validate(swagger: Swagger): Violation? {
        return validate()
    }

    fun validate(): Violation {
        return Violation(this, title, description, violationType, url, emptyList())
    }
}
