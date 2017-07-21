package de.zalando.zally.rule

import de.zalando.zally.dto.ViolationType
import io.swagger.models.Swagger

class InvalidApiSchemaRule : AbstractRule() {
    override val title = "OpenAPI 2.0 schema"
    override val violationType = ViolationType.MUST
    override val url = "http://zalando.github.io/restful-api-guidelines/general-guidelines/GeneralGuidelines.html" +
        "#must-provide-api-reference-definition-using-openapi"
    override val code = "M000"
    val description = "Given file is not OpenAPI 2.0 compliant."

    override fun validate(swagger: Swagger): Violation? {
        return validate()
    }

    fun validate(): Violation {
        return Violation(this, title, description, violationType, url, emptyList())
    }
}
