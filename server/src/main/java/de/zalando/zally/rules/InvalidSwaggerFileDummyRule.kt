package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Swagger

class InvalidSwaggerFileDummyRule : AbstractRule() {
    override val title = "Invalid Swagger File"
    override val violationType = ViolationType.MUST
    override val url = null
    override val code = "M000"

    override fun validate(swagger: Swagger): Violation? {
        throw UnsupportedOperationException("not implemented, this is only a dummy class")
    }
}
